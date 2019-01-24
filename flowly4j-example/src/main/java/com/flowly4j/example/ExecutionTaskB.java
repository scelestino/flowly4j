package com.flowly4j.example;

import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.variables.Variables;

import static com.flowly4j.example.CustomKeys.KEY2;

public class ExecutionTaskB extends ExecutionTask {

    @Override
    public Task next() {
        return new DisjunctionA();
    }

    @Override
    protected Variables perform(String sessionId, Variables variables) {
        System.out.println(variables.get(KEY2));
        return variables;
    }

}