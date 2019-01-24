package com.flowly4j.core.repository.model;

import org.joda.time.DateTime;

public class Execution {

    public final String taskId;
    public final DateTime at;

    public Execution(String taskId, DateTime at) {
        this.taskId = taskId;
        this.at = at;
    }

    public Execution(String taskId) {
        this.taskId = taskId;
        this.at = DateTime.now();
    }

    public String taskId() { return taskId; }

}
