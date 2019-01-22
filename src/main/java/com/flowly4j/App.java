package com.flowly4j;

import com.flowly4j.demo.ExecutionTaskA;
import com.flowly4j.demo.ExecutionTaskB;
import com.flowly4j.tasks.results.TaskResult;
import com.flowly4j.variables.Variables;
import io.vavr.collection.HashMap;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {


        TaskResult execute = new ExecutionTaskB().execute("123", new Variables(HashMap.empty()));


        System.out.println(execute);

        System.out.println(new ExecutionTaskA().id());

        System.out.println( "Hello World!" );

    }






}
