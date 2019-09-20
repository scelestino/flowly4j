package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Task is something to do inside a workflow
 *
 * There is no possible to use two identical Task in the same workflow
 *
 */
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public abstract class Task {

    @Getter
    String id;

    @Getter(lazy = true)
    List<Trait> traits = traits();

    @Getter
    String internalID = UUID.randomUUID().toString();

    public Task() {
        this.id = this.getClass().getSimpleName();
    }

    public Task(String id) {
        this.id = id;
    }

    /**
     * A list of keys allowed by this task. It means that a session on this task can be
     * executed with these keys
     */
    public final List<Key> allowedKeys() {
        return internalAllowedKeys().pushAll(customAllowedKeys());
    }

    /**
     * Perform a single step inside the workflow. It depends on the task implementation
     */
    public final TaskResult execute(ExecutionContext executionContext) {
        return getTraits().foldRight(this::exec, Trait::compose).apply(executionContext);
    }

    /**
     * Check if all the keys are allowed by this task
     */
    public final Boolean accept(List<Key> keys) {
        return keys.forAll( key -> allowedKeys().contains(key) );
    }

    /**
     * Used to check identity between Tasks
     */
    public final boolean sameThan(Task that) {
        return getInternalID().equals(that.getInternalID());
    }

    /**
     * A list of tasks that follows this task
     */
    public List<Task> followedBy() {
        return getTraits().flatMap(Trait::followedBy);
    }

    /**
     *  Keys configured by Traits
     */
    protected abstract List<Key> internalAllowedKeys();

    /**
     *  Keys configured by this Task
     */
    protected abstract List<Key> customAllowedKeys();

    /**
     * Traits implemented by this task
     */
    protected abstract List<Trait> traits();

    /**
     * Whatever this task is going to do
     */
    protected abstract TaskResult exec(ExecutionContext executionContext);

}
