package com.flowly4j.core.session;

import com.flowly4j.core.tasks.Task;
import io.vavr.control.Option;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * Execution Information
 */
@ToString
public class Execution {

    /**
     * Last Task that was executed
     */
    public final String taskId;

    /**
     * When it was executed
     */
    public final DateTime at;

    /**
     * Optional message about last execution
     */
    public final Option<String> message;

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
