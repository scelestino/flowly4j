package com.flowly4j.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.mongodb.MongoDBRepository;
import com.mongodb.MongoClient;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        this.initialTask = new ExecutionTaskA();

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(SNAKE_CASE);
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        mapper.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(Instant.class, new InstantSerilizator());
        module.addDeserializer(Instant.class, new InstantDeserializator());
        mapper.registerModule(module);
        mapper.setSerializationInclusion(NON_NULL);

        this.repository = new MongoDBRepository(new MongoClient("localhost"), "flowly", "workflowA", new ObjectMapper());
        this.executionContextFactory = new ExecutionContext.ExecutionContextFactory(new Serializer(mapper));
        this.eventListeners = List.of(new ConsoleEventListener());
    }

    static class InstantSerilizator extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(Date.from(value));
        }
    }

    static class InstantDeserializator extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Instant.ofEpochMilli(p.getLongValue());
        }
    }

}
