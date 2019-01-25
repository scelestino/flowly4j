package com.flowly4j.core.repository.model;

import org.joda.time.DateTime;

public class Cancellation {

    public String reason;
    public DateTime at;

    private Cancellation(String reason, DateTime at) {
        this.reason = reason;
        this.at = at;
    }

    public static Cancellation of(String reason) {
        return new Cancellation(reason, DateTime.now());
    }

}
