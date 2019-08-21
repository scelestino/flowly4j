package com.flowly4j.core.tasks.compose.retry.stopping;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;

public interface StoppingStrategy {
    Boolean shouldRetry(ReadableExecutionContext executionContext, Attempts attempts);
}