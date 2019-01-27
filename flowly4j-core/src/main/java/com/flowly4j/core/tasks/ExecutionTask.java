package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;

/**
 * An instance of this {@link Task} will execute your code and can change the execution context.
 *
 */
public abstract class ExecutionTask extends Task {

    public abstract Task next();

    protected abstract void perform(WritableExecutionContext executionContext);

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        try {
            perform(executionContext);
            return new Continue(next());
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return List.of(next());
    }

}
