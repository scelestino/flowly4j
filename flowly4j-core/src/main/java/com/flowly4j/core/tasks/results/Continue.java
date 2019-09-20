package com.flowly4j.core.tasks.results;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.tasks.Task;
import lombok.ToString;

/**
 * Current workflow execution must be continued
 *
 */
@ToString
public class Continue implements TaskResult {

    public final Task nextTask;
    public final ExecutionContext executionContext;

    public Continue(Task nextTask, ExecutionContext executionContext) {
        this.nextTask = nextTask;
        this.executionContext = executionContext;
    }

}
