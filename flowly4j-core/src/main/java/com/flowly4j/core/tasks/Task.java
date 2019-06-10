package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;
import lombok.*;

/**
 * Task is something to do inside a workflow
 *
 * There is no possible to use two identical Task in the same workflow
 *
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Task {

    private String id;

    /**
     * Check if all the keys are allowed by this task
     */
    public Boolean accept(List<Key> keys) {
        return keys.forAll( key -> allowedKeys().contains(key) );
    }

    /**
     * Perform a single step inside the workflow. It depends on the task implementation
     */
    public abstract TaskResult execute(ExecutionContext executionContext);

    /**
     * A list of tasks that follows this task
     */
    public abstract List<Task> followedBy();

    /**
     * A list of keys allowed by this task. It means that a session on this task can be
     * executed with these keys
     */
    public abstract List<Key> allowedKeys();

}
