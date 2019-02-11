package com.flowly4j.core.tasks.results;

import com.flowly4j.core.tasks.Task;
import lombok.ToString;

/**
 * Current workflow execution must be continued but current task was skipped
 *
 */
@ToString
public class SkipAndContinue implements TaskResult {

    public final Task nextTask;

    public SkipAndContinue(Task nextTask) {
        this.nextTask = nextTask;
    }

}
