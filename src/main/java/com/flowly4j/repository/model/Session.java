package com.flowly4j.repository.model;

import com.flowly4j.tasks.Task;
import com.flowly4j.variables.Variables;
import io.vavr.control.Option;
import org.joda.time.DateTime;


public class Session {

    public String id;
    public Variables variables;
    public Option<Execution> lastExecution;
    public Option<Cancellation> cancellation;
    public DateTime createAt;
    public String status;

    public Session(String id, Variables variables, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, String status) {
        this.id = id;
        this.variables = variables;
        this.lastExecution = lastExecution;
        this.cancellation = cancellation;
        this.createAt = createAt;
        this.status = status;
    }

    public Session(String id, Variables variables) {
        this.id = id;
        this.variables = variables;
        this.lastExecution = Option.none();
        this.cancellation = Option.none();
        this.createAt = DateTime.now();
        this.status = Status.CREATED;
    }

    public Boolean isExecutable() {
        switch (this.status) {
            case Status.RUNNING:
            case Status.FINISHED:
            case Status.CANCELLED:
                return false;
            default: return true;
        }
    }

    public Session running(Task task, Variables variables) {
        return changeStatus(task, variables, Status.RUNNING);
    }

    public Session blocked(Task task) {
        return changeStatus(task, variables, Status.BLOCKED);
    }

    public Session finished(Task task) {
        return changeStatus(task, variables, Status.FINISHED);
    }

    public Session onError(Task task, Throwable throwable) {
        return changeStatus(task, variables, Status.ERROR);
    }

    public Session cancelled(String reason) {
        return new Session(id, variables, lastExecution, Option.of(new Cancellation(reason)), createAt, Status.CANCELLED);
    }

    private Session changeStatus(Task task, Variables variables, String status) {
        return new Session(id, variables, Option.of(new Execution(task.id())), cancellation, createAt, status);
    }

}
