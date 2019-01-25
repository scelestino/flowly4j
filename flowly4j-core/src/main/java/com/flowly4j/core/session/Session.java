package com.flowly4j.core.session;

import com.flowly4j.core.Param;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.joda.time.DateTime;


public class Session {

    public String id;
    public Map<String, Object> variables;
    public Option<Execution> lastExecution;
    public Option<Cancellation> cancellation;
    public DateTime createAt;
    public Status status;
    public Long version;

    public Session() {
    }

    public Session(String id, Map<String, Object> variables, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, Status status, Long version) {
        this.id = id;
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
        return new Session(id, executionContext.variables(), Option.of(Execution.of(task)), cancellation, createAt, Status.RUNNING, version);
    }

    public Session blocked(Task task) {
        return new Session(id, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.BLOCKED, version);
    }

    public Session finished(Task task) {
        return new Session(id, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.FINISHED, version);
    }

    public Session onError(Task task, Throwable throwable) {
        return new Session(id, variables, Option.of(Execution.of(task, throwable.getMessage())), cancellation, createAt, Status.ERROR, version);
    }

//    public Session cancelled(String reason) {
//        return new Session(id, variables, lastExecution, Option.of(Cancellation.of(reason)), createAt, Status.CANCELLED, version + 1);
//    }


    public static Session of(String id, Param... params) {
        Map<String, Object> variables = List.of(params).toMap(p -> Tuple.of(p.key, p.value));
        return new Session(id, variables, Option.none(), Option.none(), DateTime.now(), Status.CREATED, 0L);
    }

}
