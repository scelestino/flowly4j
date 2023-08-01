package com.flowly4j.examplemariadb;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.compose.alternative.Alternative;
import com.flowly4j.core.tasks.compose.condition.Condition;
import com.flowly4j.core.tasks.compose.retry.Retry;
import io.vavr.Function1;
import io.vavr.collection.List;

import java.time.Instant;

import static com.flowly4j.examplemariadb.CustomKeys.*;

public class ExecutionTaskA extends ExecutionTask {

    @Override
    public Task next() {
        return new ExecutionTaskB();
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        System.out.println("XXXXXXX");
        executionContext.set(KEY1, "esta es una prueba");
        executionContext.set(KEY2, 123);
        executionContext.set(KEY4, Person.of("juan", 123, Instant.now()));
        throw new CustomException("se rompe");
    }

    @Override
    protected List<Key> customAllowedKeys() {
        return List.of(KEY2, KEY1, KEY6);
    }

    @Override
    protected List<Function1<ExecutionTask, Trait>> customTraits() {
        return List.of(Condition.of(c -> c.contains(KEY6)), Alternative.of(new FinishC()), Retry.of(Strategies.NOW, Strategies.TEN_TIMES));
    }

}