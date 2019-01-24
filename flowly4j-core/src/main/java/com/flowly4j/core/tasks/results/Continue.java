package com.flowly4j.core.tasks.results;

import com.flowly4j.core.tasks.Task;
import lombok.ToString;

/**
 * Current workflow execution must be continued
 *
 */
@ToString
public class Continue implements TaskResult {

    public final Task nextTask;

    public Continue(Task nextTask) {
        this.nextTask = nextTask;
    }

}
