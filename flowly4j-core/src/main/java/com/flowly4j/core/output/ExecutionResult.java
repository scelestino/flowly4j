package com.flowly4j.core.output;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.flowly4j.core.tasks.Task;
import lombok.*;

/**
 * Result of a Workflow execution
 */
@Value(staticConstructor = "of")
public class ExecutionResult {

    String sessionId;
    String taskId;
    Status status;
    ReadableExecutionContext executionContext;

    public static ExecutionResult of(Session session, Task task, ExecutionContext executionContext) {
        return new ExecutionResult(session.getSessionId(), task.getId(), session.getStatus(), executionContext);
    }

}



