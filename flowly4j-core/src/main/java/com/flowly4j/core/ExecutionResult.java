package com.flowly4j.core;

import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.flowly4j.core.tasks.Task;
import lombok.ToString;


/**
 * Result of a Workflow execution
 */
@ToString
public class ExecutionResult {

    public final String sessionId;
    public final String taskId;
    public final Status status;

    public ExecutionResult(String sessionId, String taskId, Status status) {
        this.sessionId = sessionId;
        this.taskId = taskId;
        this.status = status;
    }

    public static ExecutionResult of(Session session, Task task) {
        return new ExecutionResult(session.sessionId, task.id(), session.status);
    }

}