package com.flowly4j.core.tasks.compose.condition;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.results.SkipAndContinue;
import com.flowly4j.core.tasks.results.TaskResult;
import io.vavr.Function1;

public class Condition implements Trait {

    private HasNext parent;
    private Function1<ReadableExecutionContext, Boolean> condition;

    private Condition(HasNext parent, Function1<ReadableExecutionContext, Boolean> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    @Override
    public Function1<ExecutionContext, TaskResult> compose(Function1<ExecutionContext, TaskResult> next) {
        return context -> {
            System.out.println("CONDITION");
            return condition.apply(context) ? next.apply(context) : new SkipAndContinue(parent.next());
        };
    }

    public static <T extends HasNext> Function1<T, Trait> of(Function1<ReadableExecutionContext, Boolean> c) {
        return (T parent) -> new Condition(parent, c);
    }

}
