package com.flowly4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowly4j.demo.ExecutionTaskA;
import com.flowly4j.demo.ExecutionTaskB;
import com.flowly4j.demo.WorkflowA;
import com.flowly4j.tasks.results.TaskResult;
import com.flowly4j.variables.Variables;
import io.vavr.collection.HashMap;
import io.vavr.control.Either;

import java.io.IOException;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {


        ObjectMapper objectMapper = new ObjectMapper();


        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("1", 123);
        variables.put("2", true);
        variables.put("3", new Persona("pepe", 123));

        String json = objectMapper.writeValueAsString(variables);

        Map map = objectMapper.readValue(json, Map.class);


//        Workflow workflow = new WorkflowA();
//
//        String sessionId = workflow.init();
//
//        ExecutionResult result = workflow.execute(sessionId);


        System.out.println(json);


        System.out.println(map);


        Persona persona = objectMapper.readValue(objectMapper.writeValueAsString(map.get("3")), Persona.class);


        System.out.println(persona);

//        System.out.println( result );

    }






}
