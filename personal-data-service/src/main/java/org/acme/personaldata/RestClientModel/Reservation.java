package org.acme.personaldata.RestClientModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reservation {
    public LocalDateTime term;
}
