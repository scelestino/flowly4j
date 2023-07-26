package com.flowly4j.mariadb;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@AllArgsConstructor(staticName = "of", onConstructor = @__(@JsonCreator))
@FieldNameConstants
public class Product {

    @NonNull
    String transactionId;

    @NonNull
    String type;

    @NonNull
    String id;

}