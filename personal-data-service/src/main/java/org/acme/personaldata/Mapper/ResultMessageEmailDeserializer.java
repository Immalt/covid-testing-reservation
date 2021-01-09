package org.acme.personaldata.Mapper;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class ResultMessageEmailDeserializer extends ObjectMapperDeserializer<ResultMessageEmailDeserializer> {

    public ResultMessageEmailDeserializer() {
        super(ResultMessageEmailDeserializer.class);
    }
}
