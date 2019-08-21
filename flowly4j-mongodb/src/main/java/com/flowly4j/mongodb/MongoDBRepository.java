package com.flowly4j.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import com.mongodb.MongoClient;
import com.mongodb.client.model.IndexOptions;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import java.util.HashMap;

/**
 * MongoDB Repository implementation
 *
 * It uses Optimistic Lock to handle race condition
 *
 */
@FieldDefaults( level = AccessLevel.PROTECTED, makeFinal = true)
public class MongoDBRepository implements Repository {

    JacksonMongoCollection<Session> collection;
    ObjectMapper objectMapper;

    public MongoDBRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper objectMapper) {

        // Configure Object Mapper in order to work with Session
        this.objectMapper = objectMapper;
        this.objectMapper.addMixIn(Session.class, SessionMixIn.class);

        val mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);

        JacksonMongoCollection.JacksonMongoCollectionBuilder<Session> builder = JacksonMongoCollection.builder();
        this.collection = builder.withObjectMapper(this.objectMapper).build(mongoCollection, Session.class);

        // Initialize sessionId index
        this.collection.createIndex(new Document("sessionId", 1), new IndexOptions().unique(true));

    }

    /**
     * Load a workflow session by sessionId
     */
    @Override
    public Option<Session> get(String sessionId) {
        try {
            return Option.of(collection.findOne(new Document("sessionId", sessionId)));
        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting session " + sessionId, throwable);
        }
    }

    /**
     * Insert a new session into the database, due to the sessionId index there is no possible to create two
     * sessions with the same getId
     */
    @Override
    public Session insert(Session session) {
        try {
            collection.insert(session);
            return session;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error inserting session " + session.getSessionId(), throwable);
        }
    }

    /**
     * Update an existent session, it uses the session version to avoid inconsistencies if two threads try to
     * update the same session simultaneously
     *
     * Each time a session is updated, its version increase in one unit
     */
    @Override
    public Session update(Session session) {

        try {

            // Update will replace every document field and it is going to increment in one unit its version
            val document = JacksonMongoCollection.convertToDocument(session, objectMapper, Session.class);
            document.remove("version");

            val update = new Document("$set", document);
            update.put("$inc", new Document("version", 1));

            // Condition: there is a session with the same sessionId and version
            val query = new HashMap<String, Object>() {
                {
                    put("sessionId", session.getSessionId());
                    put("version", session.getVersion());
                }
            };

            val result = collection.findAndModify(new Document(query), new Document(), new Document(), collection.serializeFields(update), true, false);

            // if the session doesn't exist or the version is different there is no result
            if (result == null) {
                throw new OptimisticLockException("Session " + session.getSessionId() + " was modified by another transaction");
            }

            return result;

        } catch (OptimisticLockException ex) {
            throw  ex;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error saving session " + session.getSessionId(), throwable);
        }

    }

}
