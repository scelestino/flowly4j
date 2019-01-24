package com.flowly4j.core.errors;

import lombok.ToString;

@ToString
public class SessionCantBeExecuted extends RuntimeException {

    public String sessionId;

    public SessionCantBeExecuted(String sessionId) {
        this.sessionId = sessionId;
    }

}
