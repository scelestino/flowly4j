package com.flowly4j.mongodb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.session.Session;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import io.vavr.jackson.datatype.VavrModule;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import java.util.HashMap;
import java.util.Map;


public class MongoDBRepository implements Repository {

    private final JacksonMongoCollection<Session> collection;
    private final ObjectMapper objectMapper;

    public MongoDBRepository() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase database = mongo.getDatabase("flowly");
        MongoCollection<Document> workflowA = database.getCollection("workflowA");

        JacksonMongoCollection.JacksonMongoCollectionBuilder<Session> builder = JacksonMongoCollection.builder();
        collection = builder.withObjectMapper(objectMapper).build(workflowA, Session.class);

        collection.createIndex(new Document("sessionId", 1), new IndexOptions().unique(true));

    }

    @Override
    public Session get(String sessionId) {
        try {
            return collection.findOne(new Document("sessionId", sessionId));
        } catch (Throwable throwable) {
            throw new PersistenceException("Error getting session " + sessionId, throwable);
        }
    }

    @Override
    public Session insert(Session session) {
        try {
            collection.insert(session);
            return session;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error inserting session " + session.sessionId, throwable);
        }
    }

    @Override
    public Session update(Session session) {

        try {

            Document document = JacksonMongoCollection.convertToDocument(session, objectMapper, Session.class);
            document.remove("version");

            Document update = new Document("$set", document);
            update.put("$inc", new Document("version", 1));

            Map<String, Object> query = new HashMap<String, Object>() {
                {
                    put("sessionId", session.sessionId);
                    put("version", session.version);
                }
            };

            Session result = collection.findAndModify(new Document(query), new Document(), new Document(), collection.serializeFields(update), true, false);

            if (result == null) {
                throw new OptimisticLockException("Session " + session.sessionId + " was modified by another transaction");
            }

            return result;

        } catch (OptimisticLockException ex) {
            throw  ex;
        } catch (Throwable throwable) {
            throw new PersistenceException("Error saving session " + session.sessionId, throwable);
        }

    }

}
