package org.acme.personaldata.exception;

public class CannotListReservation extends Exception {
    public CannotListReservation() {
        super();
    }

    public CannotListReservation(Exception e)
    {
        super(e);
    }
}
