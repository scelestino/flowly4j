package com.flowly4j.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.mongodb.MongoDBRepository;
import com.mongodb.MongoClient;
import io.vavr.collection.List;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        this.initialTask = new ExecutionTaskA();
        this.repository = new MongoDBRepository(new MongoClient("localhost"), "flowly", "workflowA", new ObjectMapper());
        this.executionContextFactory = new ExecutionContext.ExecutionContextFactory(new Serializer(new ObjectMapper()));
        this.eventListeners = List.of(new ConsoleEventListener());
    }

}
