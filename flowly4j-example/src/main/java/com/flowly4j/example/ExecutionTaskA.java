package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import com.flowly4j.core.tasks.compose.alternative.Alternative;
import com.flowly4j.core.tasks.compose.condition.Condition;
import com.flowly4j.core.tasks.compose.retry.Retry;
import com.flowly4j.core.tasks.compose.skippable.Skippable;
import io.vavr.Function1;
import io.vavr.collection.List;

import java.time.Instant;

import static com.flowly4j.example.CustomKeys.*;

public class ExecutionTaskA extends ExecutionTask {

    public ExecutionTaskA() {
        super(Condition.of(c -> c.contains(KEY6)), Skippable.of(), Retry.of(Strategies.TWO_MINUTES, Strategies.TEN_TIMES), Alternative.of(new ExecutionTaskB()));
    }

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
    }

    @Override
    protected List<Key> customAllowedKeys() {
        return List.of(KEY2, KEY1, KEY6);
    }

}