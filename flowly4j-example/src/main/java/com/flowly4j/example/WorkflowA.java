package com.flowly4j.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.serialization.JsonSerializer;
import com.flowly4j.mongodb.MongoDBRepository;
import com.mongodb.MongoClient;
import io.vavr.jackson.datatype.VavrModule;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        this.initialTask = new ExecutionTaskA();
        this.repository = new MongoDBRepository(new MongoClient("localhost"), "flowly", "workflowA", new ObjectMapper());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule());
        objectMapper.registerModule(new JodaModule());
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        this.serializer = new JsonSerializer(objectMapper);

    }

}
