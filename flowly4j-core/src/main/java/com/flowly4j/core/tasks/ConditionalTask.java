package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;

/**
 * An instance of this {@link Task} will execute your code and can change the execution context only if
 * the condition is given, otherwise will be skipped
 *
 */
public abstract class ConditionalTask extends Task {

    public ConditionalTask(String id) {
        super(id);
    }

    protected abstract Task next();

    protected abstract void perform(WritableExecutionContext executionContext);

    protected abstract Boolean condition(ReadableExecutionContext executionContext);

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        try {
            if(condition(executionContext)) {
                perform(executionContext);
            }
            return new Continue(next());
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return List.of(next());
    }

    @Override
    protected List<Key> allowedKeys() {
        return List.empty();
    }

}
