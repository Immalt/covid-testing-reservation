package org.acme.notification.DTO;

import org.acme.notification.Enum.EmailType;

import javax.validation.constraints.NotNull;

public class ReservationEmail {
    @NotNull
    public EmailType emailType;

    @NotNull
    public Reservation emailData;
}
