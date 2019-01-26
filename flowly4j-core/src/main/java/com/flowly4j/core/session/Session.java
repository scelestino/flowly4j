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

@ToString
public class Session {

    public final String sessionId;
    public final Map<String, Object> variables;
    public final Option<Execution> lastExecution;
    public final Option<Cancellation> cancellation;
    public final DateTime createAt;
    public final Status status;
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

    public Boolean isExecutable() {
        return this.status.isExecutable();
    }

    public Session running(Task task, ExecutionContext executionContext) {
        return new Session(sessionId, executionContext.variables(), Option.of(Execution.of(task)), cancellation, createAt, Status.RUNNING, version);
    }

    public Session blocked(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.BLOCKED, version);
    }

    public Session finished(Task task) {
        return new Session(sessionId, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.FINISHED, version);
    }

    public Session onError(Task task, Throwable throwable) {
        return new Session(sessionId, variables, Option.of(Execution.of(task, throwable.getMessage())), cancellation, createAt, Status.ERROR, version);
    }

//    public Session cancelled(String reason) {
//        return new Session(sessionId, variables, lastExecution, Option.of(Cancellation.of(reason)), createAt, Status.CANCELLED, version + 1);
//    }


    /**
     * Create a new session based on parameters
     *
     */
    public static Session of(Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.key, p.value));
        return new Session(UUID.randomUUID().toString(), variables, Option.none(), Option.none(), DateTime.now(), Status.CREATED, 0L);
        //return new Session("123", variables, Option.none(), Option.none(), DateTime.now(), Status.CREATED, 0L);
    }

}
