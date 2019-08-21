package com.flowly4j.core.tasks.compose.retry.scheduling;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;

import java.time.Instant;

public class ConstantSchedulingStrategy implements SchedulingStrategy {

    private Integer secondsToRetry;

    public ConstantSchedulingStrategy(Integer secondsToRetry) {
        this.secondsToRetry = secondsToRetry;
    }

    @Override
    public Instant nextRetry(ReadableExecutionContext executionContext, Attempts attempts) {
        return Instant.now().plusSeconds(secondsToRetry);
    }

}
