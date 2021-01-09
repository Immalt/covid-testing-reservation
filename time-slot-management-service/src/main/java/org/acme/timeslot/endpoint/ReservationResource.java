package org.acme.timeslot.endpoint;

import org.acme.timeslot.entity.Reservation;
import org.eclipse.microprofile.opentracing.Traced;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
