package com.flowly4j.mongodb;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.repository.model.Session;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vavr.jackson.datatype.VavrModule;
import org.bson.Document;
import org.mongojack.JacksonMongoCollection;

public class MongoDBRepository implements Repository {

    private final JacksonMongoCollection<Session> collection;

    public MongoDBRepository() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);


        MongoClient mongo = new MongoClient("localhost");
        MongoDatabase database = mongo.getDatabase("flowly");
        MongoCollection<Document> workflowA = database.getCollection("workflowA");

        JacksonMongoCollection.JacksonMongoCollectionBuilder<Session> builder = JacksonMongoCollection.builder();
        collection = builder.withObjectMapper(objectMapper).build(workflowA, Session.class);

    }

    @Override
    public Session get(String sessionId) {
        return collection.findOne(new Document("_id", sessionId));
    }

    @Override
    public Session save(Session session) {
        collection.save(session, WriteConcern.ACKNOWLEDGED);
        return session;
    }

}
