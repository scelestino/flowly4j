package com.flowly4j.example;

import com.flowly4j.core.tasks.DisjunctionTask;
import io.vavr.collection.List;

public class DisjunctionA extends DisjunctionTask {

    public DisjunctionA() {
        super("DisjunctionA");
    }

    @Override
    protected List<Branch> branches() {
        return Branch.of(c -> true, new BlockingA(), new FinishA());
    }

}