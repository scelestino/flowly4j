package com.flowly4j.core.session;

import com.flowly4j.core.tasks.Task;
import io.vavr.control.Option;
import org.joda.time.DateTime;

public class Execution {

    public String taskId;
    public DateTime at;
    public Option<String> message;

    public Execution() {
    }

    private Execution(String taskId, DateTime at, Option<String> message) {
        this.taskId = taskId;
        this.at = at;
        this.message = message;
    }

    public static Execution of(Task task) {
        return new Execution(task.id(), DateTime.now(), Option.none());
    }

    public static Execution of(Task task, String message) {
        return new Execution(task.id(), DateTime.now(), Option.of(message));
    }

}
