package com.flowly4j.core.session;

import com.flowly4j.core.tasks.Task;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * Execution Information
 */
@Getter
@ToString
public class Execution {

    /**
     * Last Task that was executed
     */
    private String taskId;

    /**
     * When it was executed
     */
    private DateTime at;

    /**
     * Optional message about last execution
     */
    private Option<String> message;

    private Execution(String taskId, DateTime at, Option<String> message) {
        this.taskId = taskId;
        this.at = at;
        this.message = message;
    }

    public static Execution of(Task task) {
        return new Execution(task.getId(), DateTime.now(), Option.none());
    }

    public static Execution of(Task task, String message) {
        return new Execution(task.getId(), DateTime.now(), Option.of(message));
    }

}
