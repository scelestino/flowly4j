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

    private final List<Trait> traits;

    @SafeVarargs
    public ExecutionTask(Function1<ExecutionTask, Trait>... fs) {
        this.traits = List.of(fs).map(f -> f.apply(this)).reverse();
    }

    @SafeVarargs
    public ExecutionTask(String id, Function1<ExecutionTask, Trait>... fs) {
        super(id);
        this.traits = List.of(fs).map(f -> f.apply(this)).reverse();
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
        return traits.flatMap(Trait::allowedKeys);
    }

    /**
     *  Keys configured by this Task
     */
    @Override
    protected List<Key> customAllowedKeys() {
        return List.empty();
    }

    protected abstract void perform(WritableExecutionContext executionContext);

}
