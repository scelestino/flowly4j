package com.flowly4j.mongodb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.mongojack.internal.object.document.DocumentObjectGenerator;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class CustomDateModule extends SimpleModule {

    public CustomDateModule() {
        super("CustomDateModule");
        addSerializer(Instant.class, new InstantJsonSerializer());
        addSerializer(Date.class, new DateJsonSerializer());
        addDeserializer(Instant.class, new InstantJsonDeserializer());
    }

    class InstantJsonSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(Date.from(value));
        }
    }

    class InstantJsonDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return ((Date)p.getEmbeddedObject()).toInstant();
        }
    }

    class DateJsonSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if(gen instanceof DocumentObjectGenerator) {
                gen.writeObject(value);
            } else {
                gen.writeString(value.toString());
            }
        }
    }

}

