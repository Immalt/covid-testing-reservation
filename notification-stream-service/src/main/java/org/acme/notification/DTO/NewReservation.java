package org.acme.notification.DTO;

public class NewReservation extends Reservation {
    public String cancellationLink;

    public NewReservation() {}

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
