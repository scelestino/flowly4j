package com.flowly4j.example;


import com.flowly4j.core.ExecutionResult;
import com.flowly4j.core.Param;
import com.flowly4j.core.Workflow;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY2;


/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123));

        ExecutionResult result = workflow.execute(sessionId);

        System.out.println( result );

    }






}
