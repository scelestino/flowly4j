package com.flowly4j.core.session;

import lombok.Data;
import lombok.ToString;
import org.joda.time.DateTime;

@ToString
public class Cancellation {

    public final String reason;
    public final DateTime at;

    private Cancellation(String reason, DateTime at) {
        this.reason = reason;
        this.at = at;
    }

    public static Cancellation of(String reason) {
        return new Cancellation(reason, DateTime.now());
    }

}
