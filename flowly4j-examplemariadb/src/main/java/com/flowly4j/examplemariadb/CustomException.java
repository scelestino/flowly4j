package com.flowly4j.examplemariadb;

import com.flowly4j.core.tasks.compose.retry.stopping.Retryable;

public class CustomException extends RuntimeException implements Retryable {

    public CustomException(String message) {
        super(message);
    }

    @Override
    public Boolean canBeRetried() {
        return true;
    }

}
