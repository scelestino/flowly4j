package com.flowly4j.example;

import lombok.*;

import java.time.Instant;

@Value(staticConstructor = "of")
public class Person {
    String name;
    Integer age;
    Instant bla;
}

