package com.flowly4j.core.tasks.compose.retry.stopping;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;

public class QuantityStoppingStrategy implements StoppingStrategy {

    private Integer maxAttempts;

    public QuantityStoppingStrategy(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public Boolean shouldRetry(ReadableExecutionContext executionContext, Attempts attempts) {
        return attempts.getQuantity() < maxAttempts;
    }

}