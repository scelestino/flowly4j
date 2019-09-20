package com.flowly4j.example;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.tasks.compose.retry.scheduling.ConstantSchedulingStrategy;
import com.flowly4j.core.tasks.compose.retry.scheduling.SchedulingStrategy;
import com.flowly4j.core.tasks.compose.retry.stopping.QuantityStoppingStrategy;
import com.flowly4j.core.tasks.compose.retry.stopping.StoppingStrategy;

import java.time.Instant;

public class Strategies {

    static StoppingStrategy TEN_TIMES = new QuantityStoppingStrategy(10);

    static SchedulingStrategy TWO_MINUTES = new ConstantSchedulingStrategy(120);

    static SchedulingStrategy NOW = new SchedulingStrategy() {
        @Override
        public Instant nextRetry(ReadableExecutionContext executionContext, Attempts attempts) {
            return Instant.now();
        }
    };

    static StoppingStrategy NEVER = new StoppingStrategy() {
        @Override
        public Boolean shouldRetry(ReadableExecutionContext executionContext, Attempts attempts) {
            return false;
        }
    };

}
