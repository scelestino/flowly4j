package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.HasNext;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

/**
 * An instance of this {@link Task} will execute your code and can change the execution context.
 */
public abstract class ExecutionTask extends Task implements HasNext {

    public ExecutionTask() {
    }

    public ExecutionTask(String id) {
        super(id);
    }

    public abstract Task next();

    /**
     * A list of tasks that follows this task
     */
    @Override
    public final List<Task> followedBy() {
        return super.followedBy().append(next());
    }

    /**
     * Whatever this task is going to do
     */
    protected final TaskResult exec(ExecutionContext executionContext) {
        try {
            perform(executionContext);
            return new Continue(next());
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    /**
     *  Keys configured by Traits
     */
    @Override
    protected final List<Key> internalAllowedKeys() {
        return getTraits().flatMap(Trait::allowedKeys);
    }

    /**
     * Traits implemented by this task
     */
    @Override
    protected final List<Trait> traits() {
        return customTraits().map(f -> f.apply(this)).sortBy(Trait::order);
    }

    /**
     * Custom Traits implemented by this task
     */
    protected List<Function1<ExecutionTask, Trait>> customTraits() {
        return List.empty();
    }

    /**
     *  Keys configured by this Task
     */
    @Override
    protected List<Key> customAllowedKeys() {
        return List.empty();
    }

    /**
     * Whatever this task is going to do
     */
    protected abstract void perform(WritableExecutionContext executionContext);

}
