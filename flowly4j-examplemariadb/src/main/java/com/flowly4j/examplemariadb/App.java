package com.flowly4j.examplemariadb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.Workflow;
import com.flowly4j.core.context.ExecutionContext;
import com.flowly4j.core.input.Param;
import com.flowly4j.core.serialization.Serializer;
import com.flowly4j.core.session.Attempts;
import com.flowly4j.core.session.Execution;
import com.flowly4j.core.session.Session;
import com.flowly4j.core.session.Status;
import com.flowly4j.mariadb.MariaDBRepository;
import com.flowly4j.mariadb.Product;
import com.flowly4j.mongodb.CustomDateModule;
import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.control.Option;
import io.vavr.jackson.datatype.VavrModule;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.val;

import java.time.Instant;

import static com.flowly4j.examplemariadb.CustomKeys.*;


/**
 * Hello world!
 */
public class App {

    public static void main1(String[] args) {

        val objectMapperContext = new ObjectMapper();
        objectMapperContext.registerModule(new JavaTimeModule());
        objectMapperContext.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperContext.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperContext.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperContext.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        val objectMapperRepository = new ObjectMapper();
        objectMapperRepository.registerModule(new CustomDateModule());
        objectMapperRepository.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperRepository.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperRepository.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperRepository.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("com.flowly4j.examplemariadb");

        val repository = new MariaDBRepository(entityManagerFactory, objectMapperRepository);

        Execution execution = new Execution("taskId", Instant.now(), Option.of(null));
        Attempts attempts = new Attempts(1, Instant.now(), Option.of(Instant.now()));

        /*repository.insert(new Session(
                "sessionId", HashMap.of("variable_uno",
                Product.of("transactionId", "type", "id"),
                "variable_dos",
                Product.of("transactionId2", "type2", "id2")), Option.of(execution), Option.of(attempts),
                Instant.now(),
                Status.FINISHED, 1L));*/

        repository.update(new Session(
                "sessionId", HashMap.of("variable_uno",
                Product.of("transactionId", "type", "id"),
                "variable_dos",
                Product.of("transactionId2", "type2", "id2")), Option.of(execution), Option.of(attempts),
                Instant.now(),
                Status.FINISHED, 3L));

        Option<Session> s = repository.get("sessionId");
        s.forEach(System.out::println);

        val serializer = new Serializer(objectMapperContext);
        s.forEach(session -> {
            session.getVariables().forEach((k, v) -> {
                Product p = serializer.deepCopy(v, new TypeReference<Product>() {
                });
                System.out.println(p.getTransactionId());
                System.out.println(p.getId());
                System.out.println(p.getType());
            });
        });

    }

    public static void mainTestRetry(String[] args) {

        val objectMapperContext = new ObjectMapper();
        objectMapperContext.registerModule(new JavaTimeModule());
        objectMapperContext.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperContext.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperContext.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperContext.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        val objectMapperRepository = new ObjectMapper();
        objectMapperRepository.registerModule(new CustomDateModule());
        objectMapperRepository.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperRepository.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperRepository.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperRepository.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("com.flowly4j.examplemariadb");

        val repository = new MariaDBRepository(entityManagerFactory, objectMapperRepository);

        Execution execution = new Execution("taskId", Instant.now(), Option.of(null));
        Attempts attempts = new Attempts(1, Instant.now(), Option.of(Instant.now()));

        repository.insert(new Session(
                "1", HashMap.of("variable_uno",
                Product.of("transactionId", "type", "id"),
                "variable_dos",
                Product.of("transactionId2", "type2", "id2")), Option.of(execution), Option.of(attempts),
                Instant.now(),
                Status.TO_RETRY, 1L));

        repository.insert(new Session(
                "15", HashMap.of("variable_uno",
                Product.of("transactionId", "type", "id"),
                "variable_dos",
                Product.of("transactionId2", "type2", "id2")), Option.of(execution), Option.of(attempts),
                Instant.now(),
                Status.TO_RETRY, 1L));

        repository.insert(new Session(
                "2", HashMap.of("variable_uno",
                Product.of("transactionId", "type", "id"),
                "variable_dos",
                Product.of("transactionId2", "type2", "id2")), Option.of(execution), Option.of(attempts),
                Instant.now(),
                Status.FINISHED, 1L));


        Iterator<String> s = repository.getToRetry();
        s.forEach(System.out::println);

    }

    public static void main(String[] args) {
        //TODO SOLN: si usamos el mismo object mapper para repo y serializer funciona, Dory usa dos distintos

        val objectMapperRepository = new ObjectMapper();
        objectMapperRepository.registerModule(new CustomDateModule());
        objectMapperRepository.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        objectMapperRepository.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapperRepository.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapperRepository.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("com.flowly4j.examplemariadb");

        val repository = new MariaDBRepository(entityManagerFactory, objectMapperRepository);

        val factory = new ExecutionContext.ExecutionContextFactory(new Serializer(objectMapperRepository));

        System.out.println(repository.getToRetry().toList());

        Workflow workflow = new WorkflowA(repository, factory);

        String sessionId = workflow.init(Param.of(KEY1, "asd"), Param.of(KEY2, 123), Param.of(KEY6, Instant.EPOCH));

        val result = workflow.execute(sessionId);

        System.out.println(result);

    }

}
