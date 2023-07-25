package com.flowly4j.examplemariadb;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.BlockingTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.compose.condition.Condition;
import com.flowly4j.core.tasks.compose.retry.Retry;
import io.vavr.Function1;
import io.vavr.collection.List;

import static com.flowly4j.examplemariadb.CustomKeys.KEY1;
import static com.flowly4j.examplemariadb.CustomKeys.KEY4;

public class BlockingA extends BlockingTask {

    @Override
    public Boolean condition(ReadableExecutionContext executionContext) {
        System.out.println(executionContext.get(KEY4));
        return executionContext.contains(KEY1);
    }

    @Override
    public Task next() {
        return new FinishA();
    }


    @Override
    protected List<Key> customAllowedKeys() {
        return List.empty();
    }

    @Override
    protected List<Function1<BlockingTask, Trait>> customTraits() {
        return List.of(Retry.of(Strategies.TWO_MINUTES, Strategies.TEN_TIMES), Condition.of(c -> c.contains(KEY1)));
    }

}