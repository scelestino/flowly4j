package com.flowly4j.core.session;

import com.flowly4j.core.input.Param;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * It represent a workflow instance
 *
 */
@Getter
@ToString
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
     * When this session was created
     */
    private DateTime createAt;

    /**
     * Session Status
     */
    private Status status;

    /**
     * Version of this instance, can be used to implement an optimistic lock
     */
    private Long version;

    private Session(String sessionId, Map<String, Object> variables, Option<Execution> lastExecution, DateTime createAt, Status status, Long version) {
        this.sessionId = sessionId;
        this.variables = variables;
        this.lastExecution = lastExecution;
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
        return new Session(sessionId, executionContext.getVariables(), Option.of(Execution.of(task)), createAt, Status.RUNNING, version);
    }

    /**
     * Create a copy of this session with Blocked State
     */
    public Session blocked(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), createAt, Status.BLOCKED, version);
    }

    /**
     * Create a copy of this session with Finished State
     */
    public Session finished(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), createAt, Status.FINISHED, version);
    }

    /**
     * Create a copy of this session with On Error State
     */
    public Session onError(Task task, Throwable throwable) {
        return new Session(sessionId, variables, Option.of(Execution.of(task, throwable.getMessage())), createAt, Status.ERROR, version);
    }

    /**
     * Create a new session based on parameters
     */
    public static Session of(Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.getKey().getIdentifier(), p.getValue()));
        return new Session(UUID.randomUUID().toString(), variables, Option.none(), DateTime.now(), Status.CREATED, 0L);
    }

}
