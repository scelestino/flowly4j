package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.compose.HasNext;
import com.flowly4j.core.tasks.results.Block;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

/**
 * An instance of this {@link Task} could block the execution if a given condition fails.
 * <p>
 * Conditions can be setted throught the execution context.
 */
public abstract class BlockingTask extends Task implements HasNext {

    public BlockingTask() {
    }

    public BlockingTask(String id) {
        super(id);
    }

    public abstract Task next();

    /**
     * A list of tasks that follows this task
     */
    @Override
    public final List<Task> followedBy() {
        return List.of(next());
    }

    /**
     * Whatever this task is going to do
     */
    protected final TaskResult exec(ExecutionContext executionContext) {
        try {
            return condition(executionContext) ? new Continue(next()) : new Block();
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
    protected List<Function1<BlockingTask, Trait>> customTraits() {
        return List.empty();
    }

    /**
     * It blocks until given condition
     */
    protected abstract Boolean condition(ReadableExecutionContext executionContext);

}
