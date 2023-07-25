package com.flowly4j.examplemariadb;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.mongodb.CustomDateModule;
import io.vavr.jackson.datatype.VavrModule;
import lombok.val;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        //TODO SOLN: revisar esto https://discourse.hibernate.org/t/hibernate-orm-5-3-5-javax-persistence-spi-no-valid-providers-found/1249
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

        //val repository = new MariaDBRepository(entityManagerFactory, objectMapperRepository);

    }

}
