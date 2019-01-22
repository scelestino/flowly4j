package com.flowly4j.errors;

public class TaskNotFound extends RuntimeException {

    public String taskId;

    public TaskNotFound(String taskId) {
        this.taskId = taskId;
    }

}
