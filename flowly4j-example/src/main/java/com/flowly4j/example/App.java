package com.flowly4j.example;


import com.fasterxml.jackson.core.type.TypeReference;
import com.flowly4j.core.output.ExecutionResult;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.Workflow;
import lombok.val;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY2;
import static com.flowly4j.example.CustomKeys.KEY3;


/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) throws InterruptedException {


//        ExecutorService tpe = Executors.newFixedThreadPool(5);

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123));

        ExecutionResult result = workflow.execute(sessionId, Param.of(KEY3, true));
        System.out.println(result);

//        tpe.execute(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ExecutionResult result = workflow.execute(sessionId, Param.of(KEY3, true));
//            System.out.println(result);
//        });

//        tpe.execute(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            ExecutionResult result = workflow.execute(sessionId);
//            System.out.println(result);
//        });
//
//
//        tpe.awaitTermination(1000, TimeUnit.MILLISECONDS);
//
//        tpe.shutdown();

        //ExecutionResult result = workflow.execute(sessionId);

        //System.out.println( result );

    }


}
