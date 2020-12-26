package org.acme.timeslot.exception;

public class CannotListReservation extends Exception {
    public CannotListReservation() {
        super();
    }

    public CannotListReservation(Exception e)
    {
        super(e);
    }
}
