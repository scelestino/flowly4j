package com.flowly4j.core;

import com.flowly4j.core.repository.model.Session;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.variables.ReadableVariables;
import lombok.ToString;


/**
 * Result of a Workflow execution
 */
@ToString
public class ExecutionResult {

    public final String sessionId;
    public final String taskId;
    public final ReadableVariables variables;
    public final String status;

    public ExecutionResult(String sessionId, String taskId, ReadableVariables variables, String status) {
        this.sessionId = sessionId;
        this.taskId = taskId;
        this.variables = variables;
        this.status = status;
    }

    public static ExecutionResult of(Session session, Task task) {
        return new ExecutionResult(session.id, task.id(), session.variables, session.status);
    }

}
