package com.flowly4j.example;


import com.flowly4j.core.ExecutionResult;
import com.flowly4j.core.Param;
import com.flowly4j.core.Workflow;


/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(CustomKeys.KEY1, "asd"), Param.of(CustomKeys.KEY2, 123));

        ExecutionResult result = workflow.execute(sessionId);


        Param.of(CustomKeys.KEY1, "asd");
        Param.of(CustomKeys.KEY2, 123);

        System.out.println( result );

    }






}
