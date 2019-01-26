package com.flowly4j.core.session;

import com.flowly4j.core.Param;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * It represent a workflow instance
 *
 */
@ToString
public class Session {

    /**
     * Unique ID
     */
    public final String sessionId;

    /**
     * Internal variables used by the workflow, can be set from outside through params
     * or can be set from inside through the ExecutionContext
     */
    public final Map<String, Object> variables;

    /**
     * Information about the last execution of this instance
     */
    public final Option<Execution> lastExecution;

    /**
     * Information about the cancellation of this instance
     */
    public final Option<Cancellation> cancellation;

    /**
     * When this session was created
     */
    public final DateTime createAt;

    /**
     * Session Status
     */
    public final Status status;

    /**
     * Version of this instance, can be used to implement an optimistic lock
     */
    public final Long version;

    private Session(String sessionId, Map<String, Object> variables, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, Status status, Long version) {
        this.sessionId = sessionId;
        this.variables = variables;
        this.lastExecution = lastExecution;
        this.cancellation = cancellation;
        this.createAt = createAt;
        this.status = status;
        this.version = version;
    }

    /**
     * This session can be executed if meet some requirements
     */
    public Boolean isExecutable() {
        return this.status.isExecutable();
    }

    /**
     * Create a copy of this session with Running State
     */
    public Session running(Task task, ExecutionContext executionContext) {
        return new Session(sessionId, executionContext.variables(), Option.of(Execution.of(task)), cancellation, createAt, Status.RUNNING, version);
    }

    /**
     * Create a copy of this session with Blocked State
     */
    public Session blocked(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.BLOCKED, version);
    }

    /**
     * Create a copy of this session with Finished State
     */
    public Session finished(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.FINISHED, version);
    }

    /**
     * Create a copy of this session with On Error State
     */
    public Session onError(Task task, Throwable throwable) {
        return new Session(sessionId, variables, Option.of(Execution.of(task, throwable.getMessage())), cancellation, createAt, Status.ERROR, version);
    }

    /**
     * Create a copy of this session with Cancelled State
     */
    public Session cancelled(String reason) {
        return new Session(sessionId, variables, lastExecution, Option.of(Cancellation.of(reason)), createAt, Status.CANCELLED, version );
    }

    /**
     * Create a new session based on parameters
     */
    public static Session of(Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.key, p.value));
        return new Session(UUID.randomUUID().toString(), variables, Option.none(), Option.none(), DateTime.now(), Status.CREATED, 0L);
    }

}
