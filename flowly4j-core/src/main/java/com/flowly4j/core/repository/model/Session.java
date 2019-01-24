package com.flowly4j.core.repository.model;

import com.flowly4j.core.tasks.Task;
import io.vavr.control.Option;
import org.joda.time.DateTime;


public class Session {

    // TODO: add variables to session, as Map of String Object

    public final String id;
    public final Option<Execution> lastExecution;
    public final Option<Cancellation> cancellation;
    public final DateTime createAt;
    public final Status status;

    private Session(String id, Option<Execution> lastExecution, Option<Cancellation> cancellation, DateTime createAt, Status status) {
        this.id = id;
        this.lastExecution = lastExecution;
        this.cancellation = cancellation;
        this.createAt = createAt;
        this.status = status;
    }

    public Session(String id) {
        this.id = id;
        this.lastExecution = Option.none();
        this.cancellation = Option.none();
        this.createAt = DateTime.now();
        this.status = Status.CREATED;
    }

    public Boolean isExecutable() {
        return this.status.isExecutable();
    }

    public Session running(Task task) {
        return changeStatus(task, Status.RUNNING);
    }

    public Session blocked(Task task) {
        return changeStatus(task, Status.BLOCKED);
    }

    public Session finished(Task task) {
        return changeStatus(task, Status.FINISHED);
    }

    public Session onError(Task task, Throwable throwable) {
        return changeStatus(task, Status.ERROR);
    }

    public Session cancelled(String reason) {
        return new Session(id, lastExecution, Option.of(new Cancellation(reason)), createAt, Status.CANCELLED);
    }

    private Session changeStatus(Task task, Status status) {
        return new Session(id, Option.of(new Execution(task.id())), cancellation, createAt, status);
    }

}
