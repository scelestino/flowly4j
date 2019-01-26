package com.flowly4j.example;


import com.flowly4j.core.ExecutionResult;
import com.flowly4j.core.Param;
import com.flowly4j.core.Workflow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY2;


/**
 * Hello world!
 *
 */
public class App {



    public static void main( String[] args ) {

        ExecutorService tpe = Executors.newFixedThreadPool(5);

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123));

        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ExecutionResult result = workflow.execute(sessionId);
                System.out.println(result);
            }
        });

        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ExecutionResult result = workflow.execute(sessionId);
                System.out.println(result);
            }
        });

        //ExecutionResult result = workflow.execute(sessionId);

        //System.out.println( result );

    }






}
