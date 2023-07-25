package com.flowly4j.core.session;

import com.flowly4j.core.tasks.Task;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

/**
 * Execution Information
 */
@Getter
@ToString
@AllArgsConstructor
public class Execution {

    /**
     * Last Task that was executed
     */
    private String taskId;

    /**
     * When it was executed
     */
    private Instant at;

    /**
     * Optional message about last execution
     */
    private Option<String> message;

    public static Execution of(Task task) {
        return new Execution(task.getId(), Instant.now(), Option.none());
    }

    public static Execution of(Task task, String message) {
        return new Execution(task.getId(), Instant.now(), Option.of(message));
    }

}
