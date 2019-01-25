package com.flowly4j.core.session;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class Cancellation {

    public String reason;
    public DateTime at;

    public Cancellation() {
    }

    private Cancellation(String reason, DateTime at) {
        this.reason = reason;
        this.at = at;
    }

    public static Cancellation of(String reason) {
        return new Cancellation(reason, DateTime.now());
    }

}
