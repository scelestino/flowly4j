package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Task is something to do inside a workflow
 *
 * There is no possible to use two identical Task in the same workflow
 *
 */
@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class Task {

    String id;

    public Task() {
        this.id = this.getClass().getSimpleName();
    }

    public Task(String id) {
        this.id = id;
    }

    /**
     * Perform a single step inside the workflow. It depends on the task implementation
     */
    public abstract TaskResult execute(ExecutionContext executionContext);

    /**
     * Check if all the keys are allowed by this task
     */
    public Boolean accept(List<Key> keys) {
        return keys.forAll( key -> allowedKeys().contains(key) );
    }

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
