package com.flowly4j.demo;

import com.flowly4j.tasks.ExecutionTask;
import com.flowly4j.tasks.Task;
import com.flowly4j.variables.Variables;

import static com.flowly4j.demo.CustomKeys.KEY2;

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