package com.flowly4j.core.errors;

import lombok.Getter;


@Getter
public class SessionCantBeExecutedException extends RuntimeException {

    private String sessionId;

    public SessionCantBeExecutedException(String sessionId, String message) {
        super(message);
        this.sessionId = sessionId;
    }

}
