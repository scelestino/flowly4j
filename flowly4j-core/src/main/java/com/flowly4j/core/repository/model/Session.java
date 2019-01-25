package com.flowly4j.core.repository.model;

import com.flowly4j.core.Param;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.Task;
import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.joda.time.DateTime;


public class Session {

    public String _id;
    public Map<String, Object> variables;
    public Option<Execution> lastExecution;
    public Option<Cancellation> cancellation;
    public DateTime createAt;
    public Status status;

    private Session(String _id, Map<String, Object> variables, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, Status status) {
        this._id = _id;
        this.variables = variables;
        this.lastExecution = lastExecution;
        this.cancellation = cancellation;
        this.createAt = createAt;
        this.status = status;
    }

    public Session() {
    }

    public Boolean isExecutable() {
        return this.status.isExecutable();
    }

    public Session running(Task task, ExecutionContext executionContext) {
        return new Session(_id, executionContext.variables(), Option.of(Execution.of(task)), cancellation, createAt, Status.RUNNING);
    }

    public Session blocked(Task task) {
        return new Session(_id, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.BLOCKED);
    }

    public Session finished(Task task) {
        return new Session(_id, variables, Option.of(Execution.of(task)), cancellation, createAt, Status.FINISHED);
    }

    public Session onError(Task task, Throwable throwable) {
        return new Session(_id, variables, Option.of(Execution.of(task, throwable.getMessage())), cancellation, createAt, Status.ERROR);
    }

    public Session cancelled(String reason) {
        return new Session(_id, variables, lastExecution, Option.of(Cancellation.of(reason)), createAt, Status.CANCELLED);
    }

    public static Session of(String id, Param... params) {
        Map<String, Object> variables =  List.of(params).toMap( p -> Tuple.of(p.key, p.value) );
        return new Session(id, variables, Option.none(), Option.none(), DateTime.now(), Status.CREATED);
    }

}
