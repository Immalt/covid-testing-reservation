package org.acme.personaldata.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;

public class CustomDeserializer extends StdDeserializer<DateTime> {
    protected CustomDeserializer(Class<?> vc) {
        super(vc);
    }

    protected CustomDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected CustomDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public DateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);
        String userId = node.get("createdBy").asText();
        return null;
    }
}
