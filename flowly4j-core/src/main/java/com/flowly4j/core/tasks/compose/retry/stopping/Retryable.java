package com.flowly4j.core.tasks.compose.retry.stopping;

import io.vavr.API.Match.Pattern;

/**
 * Exceptions that are thrown by Task should be Retryable to allow it to attempt to retry it
 */
public interface Retryable {

    static Pattern<Throwable, Retryable> $Retryable(Pattern<Retryable, ?> p1) {
        return new Pattern<Throwable, Retryable>() {
            @Override
            public Retryable apply(Throwable throwable) {
                return (Retryable)throwable;
            }
            @Override
            public boolean isDefinedAt(Throwable value) {
                return value instanceof Retryable && p1.isDefinedAt((Retryable) value);
            }
        };
    }

    Boolean canBeRetried();

}
