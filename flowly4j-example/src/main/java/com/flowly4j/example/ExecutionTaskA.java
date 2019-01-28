package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.HashMap;

import static com.flowly4j.example.CustomKeys.*;

public class ExecutionTaskA extends ExecutionTask {

    @Override
    public Task next() {
        return new ExecutionTaskB();
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        executionContext.set(KEY1, "esta es una prueba");
        executionContext.set(KEY2, 123);
        executionContext.set(KEY4, Person.of("juan", 123));
    }

}