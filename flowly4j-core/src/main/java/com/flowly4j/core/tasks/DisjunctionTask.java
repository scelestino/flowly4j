package com.flowly4j.core.tasks;

import com.flowly4j.core.variables.ReadableVariables;
import com.flowly4j.core.variables.Variables;
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

    public DisjunctionTask(Task ifTrue, Task ifFalse, Function1<ReadableVariables, Boolean> condition) {
        this.branches = List.of(new Branch(condition, ifTrue), new Branch(c -> true, ifFalse));
    }

    @Override
    public TaskResult execute(String sessionId, Variables variables) {
        try {
            return Match(next(variables)).of(
                Case($Some($()), next -> new Continue(next, variables) ),
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

    private Option<Task> next(Variables variables) {
        return branches.find( branch -> branch.condition.apply(variables) ).map( branch -> branch.task );
    }

    public static class Branch {
        Function1<ReadableVariables, Boolean> condition;
        Task task;
        public Branch(Function1<ReadableVariables, Boolean> condition, Task task) {
            this.condition = condition;
            this.task = task;
        }
    }

}
