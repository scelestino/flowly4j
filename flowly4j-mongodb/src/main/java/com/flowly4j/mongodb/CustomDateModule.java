package com.flowly4j.mongodb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.mongojack.internal.object.document.DocumentObjectGenerator;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class CustomDateModule extends SimpleModule {

    public CustomDateModule() {
        super("CustomDateModule");
        addSerializer(Instant.class, new InstantJsonSerializer());
        addSerializer(Date.class, new DateJsonSerializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        addDeserializer(LocalDateTime.class, new LocalDateTimeJsonDeserializer());
        addDeserializer(Instant.class, new InstantJsonDeserializer());
        addSerializer(LocalDate.class, new LocalDateSerializer());
        addDeserializer(LocalDate.class, new LocalDateJsonDeserializer());
    }

    class InstantJsonSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(Date.from(value));
        }
    }
    
    class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        	gen.writeObject(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format (value));
        }
    }

    class InstantJsonDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
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
    
    class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {
    	 @Override
         public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    		 return LocalDateTime.parse((String)p.getValueAsString(), formatter);
         }
    }
    
    class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        	gen.writeString(value.toString());
        }
    }
    
    class LocalDateJsonDeserializer extends JsonDeserializer<LocalDate> {
   	 @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
   		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
   		 return LocalDate.parse((String)p.getValueAsString(), formatter);
        }
   }

}

