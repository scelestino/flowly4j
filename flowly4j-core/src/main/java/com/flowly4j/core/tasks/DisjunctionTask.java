package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.errors.DisjunctionTaskError;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.Block;
import com.flowly4j.core.tasks.results.Continue;
import com.flowly4j.core.tasks.results.OnError;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$Some;

/**
 * An instance of this {@link Task} will choose a branch of execution between different paths based on given conditions.
 * <p>
 * It will test each condition until find any that works. If no condition works, this {@link Task} will fail or block.
 */
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class DisjunctionTask extends Task {

    private final List<Trait> traits;
    private final List<Branch> branches;

    @SafeVarargs
    public DisjunctionTask(Function1<DisjunctionTask, Trait>... fs) {
        this.traits = List.of(fs).map(f -> f.apply(this)).reverse();
        this.branches = branches();
    }

    @SafeVarargs
    public DisjunctionTask(String id, Function1<DisjunctionTask, Trait>... fs) {
        super(id);
        this.traits = List.of(fs).map(f -> f.apply(this)).reverse();
        this.branches = branches();
    }

    @Override
    public final List<Task> followedBy() {
        return branches.map(branch -> branch.task);
    }

    @Override
    public final TaskResult execute(ExecutionContext executionContext) {
        return traits.foldRight(this::exec, Trait::compose).apply(executionContext);
    }

    private TaskResult exec(ExecutionContext executionContext) {
        try {
            return Match(next(executionContext)).of(
                    Case($Some($()), Continue::new),
                    Case($(), isBlockOnNoCondition() ? new Block() : new OnError(new DisjunctionTaskError(getId(), "There is no a valid branch for the given conditions on the task " + getId())))
            );
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    private Option<Task> next(ExecutionContext executionContext) {
        return branches.find( branch -> branch.condition.apply(executionContext) ).map( branch -> branch.task );
    }

    /**
     *  Keys configured by Traits
     */
    @Override
    protected final List<Key> internalAllowedKeys() {
        return traits.flatMap(Trait::allowedKeys);
    }

    /**
     * This Task is going to block instead of fail when there are no conditions that match
     */
    protected abstract Boolean isBlockOnNoCondition();

    /**
     * Branches supported by this Task
     */
    protected abstract List<Branch> branches();

    @Value(staticConstructor = "of")
    public static class Branch {

        Task task;
        Function1<ReadableExecutionContext, Boolean> condition;

        public static List<Branch> of(Function1<ReadableExecutionContext, Boolean> condition, Task ifTrue, Task ifFalse) {
            return List.of(Branch.of(ifTrue, condition), Branch.of(ifFalse, c -> true));
        }

    }

}
