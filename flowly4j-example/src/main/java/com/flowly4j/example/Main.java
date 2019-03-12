package com.flowly4j.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.flowly4j.core.session.Session;
import com.flowly4j.mongodb.SessionMixIn;
import io.vavr.jackson.datatype.VavrModule;
import lombok.Value;
import lombok.val;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main {

    public static void main( String[] args ) throws IOException {

        val objectMapper = new ObjectMapper();
        objectMapper.registerModule(new VavrModule(new VavrModule.Settings().deserializeNullAsEmptyCollection(true)));
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));

        JavaTimeModule module = new JavaTimeModule();
        //module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        //module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        module.addSerializer(Instant.class, new InstantSerilizator());
        module.addDeserializer(Instant.class, new InstantDeserializator());

        //objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        //objectMapper.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        //objectMapper.configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

        val p = new Prueba(LocalDateTime.now(), Instant.now());

        System.out.println(p);

        val json = objectMapper.writeValueAsString(p);

        System.out.println(json);

        val r = objectMapper.readValue(json, Prueba.class);

        System.out.println(r);


    }

    @Value(staticConstructor = "of")
    static class Prueba {
        LocalDateTime localDateTime;
        Instant instant;
    }

    static class InstantSerilizator extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(Date.from(value));
        }
    }

    static class InstantDeserializator extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return Instant.ofEpochMilli(p.getLongValue());
        }
    }

}
