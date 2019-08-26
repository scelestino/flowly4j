package com.flowly4j.core.tasks.compose.condition;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.compose.HasNext;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.SkipAndContinue;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;
import io.vavr.collection.List;

public class Condition implements Trait {

    private HasNext parent;
    private Function1<ReadableExecutionContext, Boolean> condition;

    private Condition(HasNext parent, Function1<ReadableExecutionContext, Boolean> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    @Override
    public Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next) {
        return executionContext -> {
            System.out.println("CONDITION");
            return condition.apply(executionContext) ? next.apply(executionContext) : new SkipAndContinue(parent.next());
        };
    }

    @Override
    public List<Key> allowedKeys() {
        return List.empty();
    }

    @Override
    public Integer order() {
        return 100;
    }

    public static <T extends HasNext> Function1<T, Trait> of(Function1<ReadableExecutionContext, Boolean> c) {
        return parent -> new Condition(parent, c);
    }

}
