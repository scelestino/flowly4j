package com.flowly4j.core.tasks.compose.retry.stopping;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeBasedStoppingStrategy implements StoppingStrategy {

    private Integer maxMinutesToRetry;

    public TimeBasedStoppingStrategy(Integer maxMinutesToRetry) {
        this.maxMinutesToRetry = maxMinutesToRetry;
    }

    @Override
    public Boolean shouldRetry(ReadableExecutionContext executionContext, Attempts attempts) {
        return ChronoUnit.MINUTES.between(attempts.getFirstAttempt(), Instant.now()) < maxMinutesToRetry;
    }

}
