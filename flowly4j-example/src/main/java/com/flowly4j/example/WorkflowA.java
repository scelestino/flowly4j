package com.flowly4j.example;

import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext.ExecutionContextFactory;
import com.flowly4j.core.repository.Repository;
import io.vavr.collection.List;

public class WorkflowA extends Workflow {

    public WorkflowA(Repository repository, ExecutionContextFactory executionContextFactory) {
        super(new ExecutionTaskA(), repository, executionContextFactory, List.of(new ConsoleEventListener()));
    }

}
