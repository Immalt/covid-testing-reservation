package org.acme.mailing.DTO;

import org.acme.mailing.Enum.EmailType;

import javax.validation.constraints.NotNull;

public class ReservationEmail {
    @NotNull
    public EmailType emailType;

    @NotNull
    public Reservation emailData;
}
