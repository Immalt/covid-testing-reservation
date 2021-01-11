package org.acme.notification.Deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.notification.DTO.ReservationEmail;

public class ReservationEmailDeserializer extends ObjectMapperDeserializer<ReservationEmail> {
    public ReservationEmailDeserializer() {
        super(ReservationEmail.class);
    }
}
