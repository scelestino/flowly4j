package com.flowly4j;

import com.flowly4j.repository.model.Session;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.ReadableVariables;
import lombok.ToString;

@ToString
public class ExecutionResult {

    public String sessionId;
    public String taskId;
    public ReadableVariables variables;
    public String status;

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
