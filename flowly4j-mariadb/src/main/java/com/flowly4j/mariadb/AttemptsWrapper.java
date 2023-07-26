package com.flowly4j.mariadb;

import com.flowly4j.core.session.Attempts;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@Embeddable
public class AttemptsWrapper {

    private Integer quantity;

    @Column(name = "first_attempt")
    private Instant firstAttempt;

    @Column(name = "next_retry")
    private Instant nextRetry;

    public AttemptsWrapper(Attempts attempts) {
        this.quantity = attempts.getQuantity();
        this.firstAttempt = attempts.getFirstAttempt();
        this.nextRetry = attempts.getNextRetry().getOrNull();
    }

    public Attempts toAttempts() {
        return new Attempts(quantity, firstAttempt, Option.of(nextRetry));
    }

}

