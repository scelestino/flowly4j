package com.flowly4j.demo;

import com.flowly4j.Workflow;
import com.flowly4j.repository.InMemoryRepository;

public class WorkflowA extends Workflow {

    public WorkflowA() {
        super(new ExecutionTaskA(), new InMemoryRepository());
    }

}
