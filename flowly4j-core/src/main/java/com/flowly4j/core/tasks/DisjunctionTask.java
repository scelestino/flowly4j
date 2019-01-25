package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.errors.DisjunctionTaskError;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
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

    private List<Branch> branches;

    public DisjunctionTask(List<Branch> branches) {
        this.branches = branches;
    }

    public DisjunctionTask(Branch ...branches) {
        this.branches = List.of(branches);
    }

    public DisjunctionTask(Task ifTrue, Task ifFalse, Function1<ExecutionContext, Boolean> condition) {
        this.branches = List.of(new Branch(condition, ifTrue), new Branch(c -> true, ifFalse));
    }

    @Override
    public TaskResult execute(ExecutionContext executionContext) {
        try {
            return Match(next(executionContext)).of(
                Case($Some($()), Continue::new),
                Case($(), new OnError(new DisjunctionTaskError()))
            );
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    @Override
    public List<Task> followedBy() {
        return branches.map(branch -> branch.task);
    }

    private Option<Task> next(ExecutionContext executionContext) {
        return branches.find( branch -> branch.condition.apply(executionContext) ).map( branch -> branch.task );
    }

    public static class Branch {
        public final Function1<ExecutionContext, Boolean> condition;
        public final Task task;
        public Branch(Function1<ExecutionContext, Boolean> condition, Task task) {
            this.condition = condition;
            this.task = task;
        }
    }

}
