package com.flowly4j.core.session;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * It represent a workflow instance
 *
 */
@Getter
@ToString
@AllArgsConstructor
public class Session {

    /**
     * Unique ID
     */
    private String sessionId;

    /**
     * Internal variables used by the workflow, can be set from outside through params
     * or can be set from inside through the ExecutionContext
     */
    private Map<String, Object> variables;

    /**
     * Information about the last execution of this instance
     */
    private Option<Execution> lastExecution;

    /**
     * Information about last attempts executions
     */
    private Option<Attempts> attempts;

    /**
     * When this session was created
     */
    private Instant createAt;

    /**
     * Session Status
     */
    private Status status;

    /**
     * Version of this instance, can be used to implement an optimistic lock
     */
    private Long version;

    /**
     * This session can be executed if meet some requirements
     */
    public Boolean isExecutable() {
        return this.status.isExecutable();
    }

    /**
     * Create a copy of this session with Running State
     */
    public Session resume(Task task, List<Param> params) {
        val v = params.toMap(p -> Tuple.of(p.getKey().getIdentifier(), p.getValue())).merge(variables);
        return new Session(sessionId, v, Option.of(Execution.of(task)), attempts.map(Attempts::newAttempt), createAt, Status.RUNNING, version);
    }

    /**
     * Create a copy of this session with Running State
     */
    public Session continuee(Task task, ExecutionContext executionContext) {
        return new Session(sessionId, executionContext.getVariables(), Option.of(Execution.of(task)), Option.none(), createAt, Status.RUNNING, version);
    }

    /**
     * Create a copy of this session with Blocked State
     */
    public Session blocked(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), Option.none(), createAt, Status.BLOCKED, version);
    }

    /**
     * Create a copy of this session with Finished State
     */
    public Session finished(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), Option.none(), createAt, Status.FINISHED, version);
    }

    /**
     * Create a copy of this session with On Error State
     */
    public Session onError(Task task, Throwable throwable) {
        return new Session(sessionId, variables, Option.of(Execution.of(task, throwable.getMessage())), attempts.map(Attempts::stopRetrying), createAt, Status.ERROR, version);
    }

    /**
     * Create a copy of this session with To Retry State
     */
    public Session toRetry(Task task, Throwable throwable, Attempts attempts) {
        return new Session(sessionId, variables, Option.of(Execution.of(task, throwable.getMessage())), Option.of(attempts), createAt, Status.TO_RETRY, version );
    }

    /**
     * Create a new session based on parameters
     */
    public static Session of(Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.getKey().getIdentifier(), p.getValue()));
        return new Session(UUID.randomUUID().toString(), variables, Option.none(), Option.none(), Instant.now(), Status.CREATED, 0L);
    }

}
