package com.flowly4j.core.errors;

import lombok.Getter;

@Getter
public class TaskNotFoundException extends RuntimeException {

    private String taskId;

    public TaskNotFoundException(String taskId, String message) {
        super(message);
        this.taskId = taskId;
    }

}
