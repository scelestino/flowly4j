package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.errors.DisjunctionTaskError;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.collection.List;
import io.vavr.control.Option;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Some;

/**
 * An instance of this {@link Task} will choose a branch of execution between different paths based on given conditions.
 * <p>
 * It will test each condition until find any that works. If no condition works, this {@link Task} will fail.
 */
public abstract class DisjunctionTask extends Task {

    public DisjunctionTask(String id) {
        super(id);
    }

    protected abstract List<Branch> branches();

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        try {
            return Match(next(executionContext)).of(
                Case($Some($()), Continue::new),
                Case($(), new OnError(new DisjunctionTaskError(getId(), "There is no a valid branch for the given conditions on the task " + getId())))
            );
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return branches().map(Branch::getTask);
    }

    @Override
    protected List<Key> allowedKeys() {
        return List.empty();
    }

    private Option<Task> next(ExecutionContext executionContext) {
        return branches().find( branch -> branch.getCondition().apply(executionContext) ).map(Branch::getTask);
    }

}
