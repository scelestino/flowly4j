package com.flowly4j.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.repository.Repository;
import com.flowly4j.core.serialization.Serializer;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;

public class WorkflowA extends Workflow {

    public WorkflowA(Repository repository) {
        this.initialTask = new ExecutionTaskA();

        val objectMapperContext = new ObjectMapper();
        objectMapperContext.registerModule(new JavaTimeModule());
        objectMapperContext.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperContext.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperContext.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperContext.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        this.repository = repository;
        this.executionContextFactory = new ExecutionContext.ExecutionContextFactory(new Serializer(objectMapperContext));
        this.eventListeners = List.of(new ConsoleEventListener());
    }

}
