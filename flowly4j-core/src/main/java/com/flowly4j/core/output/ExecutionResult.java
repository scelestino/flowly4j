package com.flowly4j.core.output;

import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.flowly4j.core.tasks.Task;
import lombok.Getter;
import lombok.ToString;


/**
 * Result of a Workflow execution
 */
@Getter
@ToString
public class ExecutionResult {

    private String sessionId;
    private String taskId;
    private Status status;

    public ExecutionResult(String sessionId, String taskId, Status status) {
        this.sessionId = sessionId;
        this.taskId = taskId;
        this.status = status;
    }

    public static ExecutionResult of(Session session, Task task) {
        return new ExecutionResult(session.getSessionId(), task.getId(), session.getStatus());
    }

}
