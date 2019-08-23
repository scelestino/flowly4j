package com.flowly4j.core.session;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@AllArgsConstructor
public class Attempts {

    private Integer quantity;

    private Instant firstAttempt;

    private Option<Instant> nextRetry;

    public Attempts newAttempt() {
        return new Attempts(quantity + 1, firstAttempt, nextRetry);
    }

    public Attempts stopRetrying() {
        return new Attempts(quantity, firstAttempt, Option.none());
    }

    public Attempts withNextRetry(Instant nextRetry) {
        return new Attempts(quantity, firstAttempt, Option.of(nextRetry));
    }

}
