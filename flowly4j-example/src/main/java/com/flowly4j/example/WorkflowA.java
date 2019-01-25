package com.flowly4j.example;

import com.flowly4j.core.Workflow;
import com.flowly4j.mongodb.MongoDBRepository;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        super(new ExecutionTaskA(), new MongoDBRepository());
    }

}
