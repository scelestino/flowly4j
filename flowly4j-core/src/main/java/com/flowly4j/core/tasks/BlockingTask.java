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

    private final List<Trait> traits;

    @SafeVarargs
    public BlockingTask(Function1<BlockingTask, Trait>... fs) {
        this.traits = List.of(fs).map(f -> f.apply(this)).sortBy(Trait::order);
    }

    @SafeVarargs
    public BlockingTask(String id, Function1<BlockingTask, Trait>... fs) {
        super(id);
        this.traits = List.of(fs).map(f -> f.apply(this)).sortBy(Trait::order);
    }

    public abstract Task next();

    @Override
    public final List<Task> followedBy() {
        return List.of(next());
    }

    @Override
    public final TaskResult execute(ExecutionContext executionContext) {
        return traits.foldRight(this::exec, Trait::compose).apply(executionContext);
    }

    private TaskResult exec(ExecutionContext executionContext) {
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
        return traits.flatMap(Trait::allowedKeys);
    }

    protected abstract Boolean condition(ReadableExecutionContext executionContext);

}
