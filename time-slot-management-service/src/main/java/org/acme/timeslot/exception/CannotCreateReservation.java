package org.acme.timeslot.exception;

public class CannotCreateReservation extends Exception {
    public CannotCreateReservation() {
        super();
    }

    public CannotCreateReservation(Exception e)
    {
        super(e);
    }
}
