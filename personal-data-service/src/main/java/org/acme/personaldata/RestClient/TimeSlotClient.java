package org.acme.personaldata.RestClient;

import org.acme.personaldata.RestClientModel.Reservation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.UUID;

@RegisterRestClient(configKey="timeslot-api")
@ApplicationScoped
public interface TimeSlotClient {
    @GET
    @Path("/reservation")
    public Reservation getReservation(@QueryParam("applicationId") UUID applicationId);
}
