package org.acme.mailing.Deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.mailing.DTO.ResultEmail;

public class ResultEmailDeserializer extends ObjectMapperDeserializer<ResultEmail> {
    public ResultEmailDeserializer() {
        super(ResultEmail.class);
    }
}
