package com.flowly4j.core.serialization;

public interface Serializer<K> {

    K write(Object obj);

    <T> T read(K data);

    <T> T deepCopy(Object obj);

}
