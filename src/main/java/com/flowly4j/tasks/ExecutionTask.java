package com.flowly4j.tasks;

import com.flowly4j.variables.Variables;
import com.flowly4j.tasks.results.Continue;
import com.flowly4j.tasks.results.OnError;
import com.flowly4j.tasks.results.TaskResult;
import io.vavr.collection.List;
import io.vavr.control.Either;

/**
 * An instance of this {@link Task} will execute your code and can change the execution context.
 *
 */
public abstract class ExecutionTask extends Task {

    public abstract Task next();

    protected abstract Either<Throwable, Variables> perform(String sessionId, Variables variables);

    @Override
    public TaskResult execute(String sessionId, Variables variables) {
        try {
            return perform(sessionId, variables).fold(OnError::new, v -> new Continue(next(), v));
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return List.of(next());
    }

}
