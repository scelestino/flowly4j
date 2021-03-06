package com.flowly4j.core.errors;

import lombok.Getter;

@Getter
public class DisjunctionTaskError extends RuntimeException {

    private String taskId;

    public DisjunctionTaskError(String taskId, String message) {
        super(message);
        this.taskId = taskId;
    }

}
