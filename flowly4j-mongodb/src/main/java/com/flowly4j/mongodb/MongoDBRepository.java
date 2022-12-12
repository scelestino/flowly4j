package com.flowly4j.mongodb;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReturnDocument;

import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.JacksonMongoCollection.JacksonMongoCollectionBuilder;
import org.mongojack.MongoJsonMappingException;
import org.mongojack.internal.object.document.DocumentObjectGenerator;
import org.mongojack.internal.stream.JacksonDBObject;
import org.mongojack.internal.util.DocumentSerializationUtils;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
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
    MongoCollection<Document> mongoCollection;

    public MongoDBRepository(MongoClient client, String databaseName, String collectionName, ObjectMapper objectMapper) {
    	
        // Configure Object Mapper in order to work with Session
        this.objectMapper = objectMapper;
        this.objectMapper.addMixIn(Session.class, SessionMixIn.class);
        
        this.mongoCollection = client.getDatabase(databaseName).getCollection(collectionName);
        
        this.collection = JacksonMongoCollection.builder()
			    .withObjectMapper(objectMapper)
			    .build(client, databaseName, collectionName, Session.class, UuidRepresentation.STANDARD);
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
        	            
            DocumentObjectGenerator generator = new DocumentObjectGenerator();
            try {
                   objectMapper.writerWithView(Session.class).writeValue(generator, session);
               } catch (JsonMappingException e) {
                   throw new MongoJsonMappingException(e);
               } catch (IOException e) {
                   // This shouldn't happen
                   throw new MongoException("Unknown error occurred converting BSON to object", e);
               }
            Document document =  generator.getDocument();
            
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
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            options.returnDocument(ReturnDocument.AFTER);
            
            val result = collection.findOneAndUpdate(new Document(query), update,options);
            
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

    @Override
    public Iterator<String> getToRetry() {

        try {

            val query = new HashMap<String, Object>() {
                {
                    put("status", Status.TO_RETRY);
                    put("attempts.nextRetry", new Document("$lte", Date.from(Instant.now())));
                }
            };

            return Iterator.ofAll(collection.find(new Document(query)).sort(new Document("attempts.nextRetry", 1)).map(Session::getSessionId));

        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting sessions to retry", throwable);
        }

    }
    
    public Document serializeFields(Document value) {
    	DocumentObjectGenerator generator = new DocumentObjectGenerator();
        try {
               objectMapper.writerWithView(Session.class).writeValue(generator, value);
           } catch (JsonMappingException e) {
               throw new MongoJsonMappingException(e);
           } catch (IOException e) {
               // This shouldn't happen
               throw new MongoException("Unknown error occurred converting BSON to object", e);
           }
        Document document =  generator.getDocument();
        return document;
    }
}
