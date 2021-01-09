package org.acme.timeslot.restClient;

import org.acme.timeslot.restClientModel.Application;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

@RegisterRestClient(configKey = "personal-data-api")
@ApplicationScoped
public interface PersonalDataClient {

    @GET
    @Path("/applications/{applicationId : [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
    public Application getApplication(@PathParam("applicationId") UUID applicationId);
}
