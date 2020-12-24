package org.acme.personaldata.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.personaldata.entity.Organization;
import org.acme.personaldata.entity.Reservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@RegisterForReflection
public class ReservationDTO {
    @NotNull
    public UUID applicationId;
    @NotNull
    public LocalDateTime term;

    public Reservation createEntity(Organization organization)
    {
        Reservation reservation = new Reservation();
        reservation.organization = organization;
        updateEntity(reservation);
        return reservation;
    }

    public void updateEntity(Reservation reservation)
    {
        reservation.term = term;
        reservation.applicationId = applicationId;
    }
}
