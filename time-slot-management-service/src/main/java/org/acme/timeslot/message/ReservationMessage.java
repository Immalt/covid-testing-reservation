package org.acme.timeslot.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationMessage {

    public String firstName;
    public String lastName;
    public String IDCardNumber;
    public String email;
    public UUID applicationId;
    public LocalDateTime term;
}
