package com.flowly4j.example;

import com.flowly4j.core.Workflow;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        super(new ExecutionTaskA(), new InMemoryRepository());
    }

}
