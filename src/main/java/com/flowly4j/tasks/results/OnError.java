package com.flowly4j.tasks.results;

import lombok.ToString;

/**
 * There was an unexpected error during current workflow execution
 *
 */
@ToString
public class OnError implements TaskResult {
    Throwable cause;
    public OnError(Throwable cause) {
        this.cause = cause;
    }
}
