package com.flowly4j.examplemariadb;

import lombok.Value;

import java.time.Instant;

@Value(staticConstructor = "of")
public class Person {
    String name;
    Integer age;
    Instant bla;
}

