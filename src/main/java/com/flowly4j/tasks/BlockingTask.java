package com.flowly4j.tasks;

import com.flowly4j.variables.ReadableVariables;
import com.flowly4j.variables.Variables;
import com.flowly4j.tasks.results.Block;
import com.flowly4j.tasks.results.Continue;
import com.flowly4j.tasks.results.OnError;
import com.flowly4j.tasks.results.TaskResult;
import io.vavr.collection.List;
import lombok.ToString;

/**
 * An instance of this {@link Task} could block the execution if a given condition fails.
 *
 * Conditions can be setted throught the execution context.
 *
 */
public abstract class BlockingTask extends Task {

    public abstract Boolean condition(ReadableVariables variables);

    public abstract Task next();

    @Override
    public TaskResult execute(String sessionId, Variables variables) {
        try {
            return condition(variables) ? new Continue(next(), variables) : new Block();
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return List.of(next());
    }

}
