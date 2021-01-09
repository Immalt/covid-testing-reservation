package org.acme.timeslot.message;

import org.acme.timeslot.enums.MessageEmailType;

public class ReservationEmailMessage {
    public MessageEmailType emailType;
    public ReservationMessage emailData;
}
