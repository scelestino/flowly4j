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
import lombok.Getter;
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

    @Getter(lazy = true)
    private final List<Branch> branches = branches();

    public DisjunctionTask() {
    }

    public DisjunctionTask(String id) {
        super(id);
    }

    /**
     * A list of tasks that follows this task
     */
    @Override
    public final List<Task> followedBy() {
        return super.followedBy().pushAll(getBranches().map(branch -> branch.task));
    }

    /**
     * Whatever this task is going to do
     */
    protected final TaskResult exec(ExecutionContext executionContext) {
        try {
            return Match(next(executionContext)).of(
                    Case($Some($()), task -> new Continue(task, executionContext)),
                    Case($( t -> isBlockOnNoCondition() ), new Block()),
                    Case($(), new OnError(new DisjunctionTaskError(getId(), "There is no a valid branch for the given conditions on the task " + getId())))
            );
        } catch (Throwable throwable) {
            return new OnError(throwable);
        }
    }

    /**
     *  Keys configured by Traits
     */
    @Override
    protected final List<Key> internalAllowedKeys() {
        return getTraits().flatMap(Trait::allowedKeys);
    }

    /**
     * Traits implemented by this task
     */
    @Override
    protected final List<Trait> traits() {
        return customTraits().map(f -> f.apply(this)).sortBy(Trait::order);
    }

    /**
     * Custom Traits implemented by this task
     */
    protected List<Function1<DisjunctionTask, Trait>> customTraits() {
        return List.empty();
    }

    /**
     * This Task is going to block instead of fail when there are no conditions that match
     */
    protected abstract Boolean isBlockOnNoCondition();

    /**
     * Branches supported by this Task
     */
    protected abstract List<Branch> branches();

    private Option<Task> next(ExecutionContext executionContext) {
        return getBranches().find( branch -> branch.condition.apply(executionContext) ).map( branch -> branch.task );
    }

    @Value(staticConstructor = "of")
    public static class Branch {

        Task task;
        Function1<ReadableExecutionContext, Boolean> condition;

        public static List<Branch> of(Function1<ReadableExecutionContext, Boolean> condition, Task ifTrue, Task ifFalse) {
            return List.of(Branch.of(ifTrue, condition), Branch.of(ifFalse, c -> true));
        }

    }

}
