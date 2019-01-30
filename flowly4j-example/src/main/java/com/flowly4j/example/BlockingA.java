package com.flowly4j.example;

import com.flowly4j.core.context.ReadableExecutionContext;
import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.BlockingTask;
import com.flowly4j.core.tasks.Task;
import io.vavr.collection.List;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY4;

public class BlockingA extends BlockingTask {

    public BlockingA() {
        super("BlockingA");
    }

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
    protected List<Key> allowedKeys() {
        return List.empty();
    }

}