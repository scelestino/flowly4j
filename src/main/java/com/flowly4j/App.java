package com.flowly4j;

import com.flowly4j.demo.ExecutionTaskA;
import com.flowly4j.demo.ExecutionTaskB;
import com.flowly4j.demo.WorkflowA;
import com.flowly4j.tasks.results.TaskResult;
import com.flowly4j.variables.Variables;
import io.vavr.collection.HashMap;
import io.vavr.control.Either;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {


        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init();

        ExecutionResult result = workflow.execute(sessionId);


        System.out.println( result );

    }






}
