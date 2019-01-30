package com.flowly4j.core.errors;

import com.flowly4j.core.session.Session;
import com.flowly4j.core.tasks.Task;
import lombok.Getter;

@Getter
public class ExecutionException extends RuntimeException {

    private Session session;
    private Task task;

    public ExecutionException(Session session, Task task, Throwable cause) {
        super(cause);
        this.session = session;
        this.task = task;
    }

}
