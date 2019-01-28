package com.flowly4j.example;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.output.ExecutionResult;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.serialization.JsonSerializer;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.flowly4j.example.CustomKeys.KEY1;
import static com.flowly4j.example.CustomKeys.KEY2;


/**
 * Hello world!
 *
 */
public class App {


    private static <T> TypeReference<T> bla() {
            return new TypeReference<T>() {};
    }

    public static void main( String[] args ) throws InterruptedException {

        ExecutorService tpe = Executors.newFixedThreadPool(5);

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123));

     //   ObjectMapper objectMapper = new ObjectMapper();
     //   objectMapper.registerModule(new VavrModule());
    //    objectMapper.registerModule(new JodaModule());
    //    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    //    val s = new JsonSerializer(objectMapper);


      //  String json = s.write(HashMap.of("asd", "ase"));



        //val r = s.<Map<String, String>>read(json);






       // System.out.println(r);

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


        tpe.awaitTermination(1000, TimeUnit.MILLISECONDS);

        tpe.shutdown();

        //ExecutionResult result = workflow.execute(sessionId);

        //System.out.println( result );

    }






}
