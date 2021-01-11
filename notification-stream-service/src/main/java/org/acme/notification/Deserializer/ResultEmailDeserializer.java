package org.acme.notification.Deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.notification.DTO.ResultEmail;

public class ResultEmailDeserializer extends ObjectMapperDeserializer<ResultEmail> {
    public ResultEmailDeserializer() {
        super(ResultEmail.class);
    }
}
