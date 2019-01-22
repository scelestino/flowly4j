package com.flowly4j.repository.model;

import org.joda.time.DateTime;

public class Cancellation {

    public String reason;
    public DateTime at;

    public Cancellation(String reason, DateTime at) {
        this.reason = reason;
        this.at = at;
    }

    public Cancellation(String reason) {
        this.reason = reason;
        this.at = DateTime.now();
    }

}
