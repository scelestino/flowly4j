package com.flowly4j.core.errors;

import com.flowly4j.core.repository.model.Session;
import com.flowly4j.core.tasks.Task;

public class ExecutionError extends RuntimeException {

    public Session session;
    public Task task;

    public ExecutionError(Throwable cause, Session session, Task task) {
        super(cause);
        this.session = session;
        this.task = task;
    }

}
