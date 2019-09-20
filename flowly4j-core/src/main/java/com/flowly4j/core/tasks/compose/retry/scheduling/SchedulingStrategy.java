package com.flowly4j.core.tasks.compose.retry.scheduling;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;

import java.time.Instant;

public interface SchedulingStrategy {
    Instant nextRetry(ReadableExecutionContext executionContext, Attempts attempts);
}