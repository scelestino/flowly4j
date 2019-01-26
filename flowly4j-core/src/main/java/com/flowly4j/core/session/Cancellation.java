package com.flowly4j.core.session;

import lombok.ToString;
import org.joda.time.DateTime;

/**
 * Cancellation Information
 */
@ToString
public class Cancellation {

    /**
     * Why this session was cancelled
     */
    public final String reason;

    /**
     * When this session was cancelled
     */
    public final DateTime at;

    private Cancellation(String reason, DateTime at) {
        this.reason = reason;
        this.at = at;
    }

    public static Cancellation of(String reason) {
        return new Cancellation(reason, DateTime.now());
    }

}
