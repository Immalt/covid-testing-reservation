package org.acme.notification.Resources;

import io.smallrye.reactive.messaging.annotations.Channel;
import org.acme.notification.DTO.ResultEmail;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.SseElementType;

@Path("/notifications")
public class NotificationResource {
    @Inject
    @Channel("result") Publisher<ResultEmail> resultEmails;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public Publisher<ResultEmail> stream() {
        return resultEmails;
    }
}
