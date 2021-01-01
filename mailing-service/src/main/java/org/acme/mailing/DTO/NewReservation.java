package org.acme.mailing.DTO;

import io.smallrye.common.constraint.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;


public class NewReservation extends Reservation {
    public String cancellationLink;

    public NewReservation(Reservation reservation, String cancellationLink)
    {
        term = reservation.term;
        email = reservation.email;
        firstName = reservation.firstName;
        lastName = reservation.lastName;
        applicationId = reservation.applicationId;
        IDCardNumber = reservation.IDCardNumber;
        this.cancellationLink = cancellationLink;
    }
}
