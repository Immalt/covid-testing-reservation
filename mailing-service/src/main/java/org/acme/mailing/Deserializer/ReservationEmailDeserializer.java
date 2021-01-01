package org.acme.mailing.Deserializer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.acme.mailing.DTO.ReservationEmail;

public class ReservationEmailDeserializer extends ObjectMapperDeserializer<ReservationEmail> {
    public ReservationEmailDeserializer() {
        super(ReservationEmail.class);
    }
}
