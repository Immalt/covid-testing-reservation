package org.acme.mailing.DTO;

import io.smallrye.common.constraint.NotNull;
import org.acme.mailing.Enum.EmailType;

public class ReservationEmail {
    @NotNull
    public EmailType emailType;

    @NotNull
    public Reservation emailData;
}
