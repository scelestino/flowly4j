package com.flowly4j.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.mongodb.CustomDateModule;
import com.flowly4j.mongodb.MongoDBRepository;
import com.mongodb.MongoClient;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        this.initialTask = new ExecutionTaskA();

        val objectMapperRepository = new ObjectMapper();
        objectMapperRepository.registerModule(new CustomDateModule());
        objectMapperRepository.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperRepository.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperRepository.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperRepository.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        val objectMapperContext = new ObjectMapper();
        objectMapperContext.registerModule(new JavaTimeModule());
        objectMapperContext.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperContext.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperContext.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperContext.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        this.repository = new MongoDBRepository(new MongoClient("localhost"), "flowly", "workflowA", objectMapperRepository);
        this.executionContextFactory = new ExecutionContext.ExecutionContextFactory(new Serializer(objectMapperContext));
        this.eventListeners = List.of(new ConsoleEventListener());
    }

}
