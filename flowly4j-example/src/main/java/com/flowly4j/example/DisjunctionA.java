package com.flowly4j.example;

import com.flowly4j.core.tasks.DisjunctionTask;
import io.vavr.collection.List;

public class DisjunctionA extends DisjunctionTask {

    public DisjunctionA() {
        super("DisjunctionA", true);
    }

    @Override
    protected List<Branch> branches() {
//        return List.of(Branch.of(c -> false, new BlockingA()), Branch.of(c -> false, new BlockingA()));
        return Branch.of(c -> true, new BlockingA(), new FinishA());
    }

}