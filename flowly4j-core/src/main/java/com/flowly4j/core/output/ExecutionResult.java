package com.flowly4j.core.output;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.flowly4j.core.tasks.Task;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Result of a Workflow execution
 */
@Getter
@ToString
@AllArgsConstructor( access = AccessLevel.PRIVATE )
public class ExecutionResult {

    private String sessionId;
    private String taskId;
    private Status status;
    private ReadableExecutionContext executionContext;

    public static ExecutionResult of(Session session, Task task, ExecutionContext executionContext) {
        return new ExecutionResult(session.getSessionId(), task.getId(), session.getStatus(), executionContext);
    }

}



