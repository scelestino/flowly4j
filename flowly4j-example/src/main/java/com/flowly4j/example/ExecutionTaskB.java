package com.flowly4j.example;

import com.flowly4j.core.context.WritableExecutionContext;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;

import java.time.Instant;

import static com.flowly4j.example.CustomKeys.*;

public class ExecutionTaskB extends ExecutionTask {

    public ExecutionTaskB() {
        super("ExecutionTaskA");
    }

    @Override
    public Task next() {
        return new DisjunctionA();
    }

    @Override
    protected void perform(WritableExecutionContext executionContext) {
        System.out.println(executionContext.get(KEY1));
        executionContext.set(KEY4, Person.of("juan", 20, Instant.now()));
        executionContext.unset(KEY2);
        Person x = executionContext.get(KEY4).get();


        System.out.println(executionContext.get(KEY6));

        System.out.println(x);
    }

}