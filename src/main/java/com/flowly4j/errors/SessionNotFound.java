package com.flowly4j.errors;

import lombok.ToString;

@ToString
public class SessionNotFound extends RuntimeException {

    public String sessionId;

    public SessionNotFound(String sessionId) {
        this.sessionId = sessionId;
    }

}
