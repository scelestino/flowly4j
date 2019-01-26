package com.flowly4j.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.core.Workflow;
import com.flowly4j.mongodb.MongoDBRepository;
import com.mongodb.MongoClient;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        super(new ExecutionTaskA(), new MongoDBRepository(new MongoClient("localhost"), "flowly", "workflowA", new ObjectMapper()));
//        super(new ExecutionTaskA(), new InMemoryRepository());
    }

}
