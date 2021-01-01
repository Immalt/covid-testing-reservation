package org.acme.mailing.Enum;

public enum EmailType {
    CANCELLED_RESERVATION {
        @Override
        public String subject() {
            return "Reservation Cancelled";
        }
    },
    CREATED_RESERVATION {
        @Override
        public String subject() {
            return "Reservation Created";
        }
    },
    RESULTS {
        @Override
        public String subject() {
            return "Your results are here!";
        }
    };


    public abstract String subject();
}
