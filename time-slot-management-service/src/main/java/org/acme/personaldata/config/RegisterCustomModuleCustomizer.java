package org.acme.personaldata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.quarkus.jackson.ObjectMapperCustomizer;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.inject.Singleton;

@Singleton
@RegisterForReflection
public class RegisterCustomModuleCustomizer implements ObjectMapperCustomizer {
    public void customize(ObjectMapper mapper) {
//        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}