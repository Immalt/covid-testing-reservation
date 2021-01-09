package org.acme.mailing.DTO;

import javax.validation.constraints.NotNull;
import org.acme.mailing.Enum.EmailType;

public class ReservationEmail {
    @NotNull
    public EmailType emailType;

    @NotNull
    public Reservation emailData;
}
