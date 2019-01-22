package com.flowly4j.errors;

import com.flowly4j.repository.model.Session;
import com.flowly4j.tasks.Task;

public class ExecutionError extends RuntimeException {

    public Session session;
    public Task task;

    public ExecutionError(Throwable cause, Session session, Task task) {
        super(cause);
        this.session = session;
        this.task = task;
    }

}
