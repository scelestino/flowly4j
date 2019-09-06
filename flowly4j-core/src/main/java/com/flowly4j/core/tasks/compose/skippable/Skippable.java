package com.flowly4j.core.tasks.compose.skippable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.HasNext;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.SkipAndContinue;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

public class Skippable implements Trait {

    private HasNext parent;

    private Skippable(HasNext parent) {
        this.parent = parent;
    }

    @Override
    public Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next) {
        return executionContext -> {
            return executionContext.get(SKIP).contains(true) ? new SkipAndContinue(parent.next()) : next.apply(executionContext.unset(SKIP));
        };
    }

    @Override
    public List<Key> allowedKeys() {
        return List.of(SKIP);
    }

    @Override
    public Integer order() {
        return 100;
    }

    @Override
    public List<Task> followedBy() {
        return List.empty();
    }

    public static <T extends HasNext> Function1<T, Trait> of() {
        return Skippable::new;
    }

    public static Key<Boolean> SKIP = Key.of("SKIP", new TypeReference<Boolean>() {});

}
