package com.flowly4j.example;


import com.flowly4j.core.input.Param;
import com.flowly4j.core.Workflow;
import lombok.val;

import java.time.Instant;

import static com.flowly4j.example.CustomKeys.*;


/**
 * Hello world!
 *
 */
public class App {

    public static void main( String[] args ) {

        Workflow workflow = new WorkflowA();

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123), Param.of(KEY6, Instant.EPOCH));

        val result = workflow.execute(sessionId);

        System.out.println(result);

    }


}
