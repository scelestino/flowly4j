package com.flowly4j.example;

import com.flowly4j.core.input.Key;
import com.flowly4j.core.tasks.DisjunctionTask;
import io.vavr.collection.List;


public class DisjunctionA extends DisjunctionTask {

    @Override
    protected Boolean isBlockOnNoCondition() {
        return true;
    }

    @Override
    protected List<Branch> branches() {
        return Branch.of(c -> true, new BlockingA(), new FinishA());
    }

    @Override
    protected List<Key> customAllowedKeys() {
        return List.empty();
    }

}