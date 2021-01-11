package org.acme.notification.Enum;

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
            return "Results Sent";
        }
    };


    public abstract String subject();
}
