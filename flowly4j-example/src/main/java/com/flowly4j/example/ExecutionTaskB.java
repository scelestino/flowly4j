package com.flowly4j.example;

import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY2;

public class ExecutionTaskB extends ExecutionTask {

    @Override
    public Task next() {
        return new DisjunctionA();
    }

    @Override
    protected void perform(ExecutionContext executionContext) {
        System.out.println(executionContext.get(KEY1));
    }

}