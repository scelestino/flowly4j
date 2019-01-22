package com.flowly4j.errors;

import lombok.ToString;

@ToString
public class TaskNotFound extends RuntimeException {

    public String taskId;

    public TaskNotFound(String taskId) {
        this.taskId = taskId;
    }

}
