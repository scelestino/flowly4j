package com.flowly4j.example;

import com.flowly4j.core.tasks.ExecutionTask;
import com.flowly4j.core.tasks.Task;
import com.flowly4j.core.tasks.compose.Trait;
import io.vavr.Function1;
import io.vavr.collection.List;
import lombok.val;

public class Demo {

    public static void main( String[] args ) {

//        List<Trait> traits = List.of(new Condtional(), new Retry());
//
//
//        Function1<Context, Result> performAsFunction = context -> new Result("ERROR");
//
//        val F = traits.foldRight( performAsFunction, Trait::compose);
//
//
//        val r = F.apply(new Context("valor loco"));
//
//        System.out.println(r);


//        metodo(a, b);
//
//
//        metodo1(a);
//        metodo1(b);
//        metodo1(d);


    }


    @SafeVarargs
    public static void metodo(Function1<Task, ExecutionTask>... fs) {
        val a = List.of(fs).map(c -> c.apply(new FinishA()) );
        System.out.println(a);
    }

    public static void metodo1(Function1<ExecutionTask, Trait> f) {
        System.out.println(f);
    }











}
