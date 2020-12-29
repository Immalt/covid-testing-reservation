package org.acme.timeslot.endpoint;

import org.acme.timeslot.dto.ReservationDTO;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.exception.CannotListReservation;
import org.acme.timeslot.service.ReservationService;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Path("/reservation")
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Traced
public class ReservationResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Reservation getReservationByApplicationId(@NotNull @QueryParam("applicationId") UUID applicationId) {
        List<Reservation> reservations = Reservation.find("applicationId", applicationId).list();
        return reservations.get(0);
    }
}
