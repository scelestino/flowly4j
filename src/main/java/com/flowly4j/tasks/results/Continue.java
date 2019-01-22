package com.flowly4j.tasks.results;

import com.flowly4j.variables.Variables;
import com.flowly4j.tasks.Task;
import lombok.ToString;

/**
 * Current workflow execution must be continued
 *
 */
@ToString
public class Continue implements TaskResult {
    Task nextTask;
    Variables variables;
    public Continue(Task nextTask, Variables variables) {
        this.nextTask = nextTask;
        this.variables = variables;
    }
}
