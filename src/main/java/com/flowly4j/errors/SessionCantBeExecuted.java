package com.flowly4j.errors;

public class SessionCantBeExecuted extends RuntimeException {

    public String sessionId;

    public SessionCantBeExecuted(String sessionId) {
        this.sessionId = sessionId;
    }

}
