package com.flowly4j.core.errors;

import lombok.Getter;

@Getter
public class SessionNotFoundException extends RuntimeException {

    private String sessionId;

    public SessionNotFoundException(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionNotFoundException(String sessionId, String message) {
        super(message);
        this.sessionId = sessionId;
    }

}
