package com.flowly4j.core.tasks;

import com.flowly4j.core.context.ReadableExecutionContext;
import io.vavr.Function1;
import io.vavr.collection.List;
import lombok.Value;

@Value(staticConstructor = "of")
public class Branch {

    String name;
    Function1<ReadableExecutionContext, Boolean> condition;
    Task task;

    public static List<Branch> of(String name, Function1<ReadableExecutionContext, Boolean> condition, Task ifTrue, Task ifFalse) {
        return List.of(Branch.of(name, condition, ifTrue), Branch.of(name, c -> true, ifFalse));
    }

}
