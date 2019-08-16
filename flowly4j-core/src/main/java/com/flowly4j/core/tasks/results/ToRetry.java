package com.flowly4j.core.tasks.results;

import com.flowly4j.core.session.Attempts;
import lombok.ToString;

/**
 * There was an unexpected error during current workflow execution
 *
 */
@ToString
public class ToRetry implements TaskResult {

    public final Throwable cause;
    public final Attempts attempts;

    public ToRetry(Throwable cause, Attempts attempts) {
        this.cause = cause;
        this.attempts = attempts;
    }

}
