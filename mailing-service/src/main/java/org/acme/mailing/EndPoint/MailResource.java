package org.acme.mailing.EndPoint;

import io.smallrye.common.constraint.NotNull;
import org.acme.mailing.DTO.NewReservation;
import org.acme.mailing.DTO.Reservation;
import org.acme.mailing.DTO.Result;
import org.acme.mailing.Enum.EmailType;
import org.acme.mailing.Enum.LinkType;
import org.acme.mailing.Exception.UnknownEmailType;
import org.acme.mailing.Exception.UnknownLinkType;
import org.acme.mailing.Factory.LinkFactory;
import org.acme.mailing.Service.EmailService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/mail")
@Consumes(MediaType.APPLICATION_JSON)
public class MailResource {
    @Inject
    EmailService emailService;

    @Inject
    LinkFactory linkFactory;

    @GET
    @Path("/active-email-types")
    public List<EmailType> activeEmailTypes()
    {
        return Arrays.asList(EmailType.values());
    }

    @POST
    @Path("/new-reservation")
    public void sendEmail(@NotNull @Valid Reservation mailInfo) throws UnknownEmailType, UnknownLinkType {
        mailInfo = new NewReservation(mailInfo, linkFactory.getLink(LinkType.CANCELLATION_LINK));
        emailService.sendEmail(EmailType.CREATED_RESERVATION, mailInfo);
    }

    @POST
    @Path("/cancelled-reservation")
    public void cancelReservation(@NotNull @Valid Reservation mailInfo) throws UnknownEmailType {
        emailService.sendEmail(EmailType.CANCELLED_RESERVATION, mailInfo);
    }

    @POST
    @Path("/result")
    public void cancelReservation(@NotNull Result mailInfo) throws UnknownEmailType {
        emailService.sendEmail(EmailType.RESULTS, mailInfo);
    }
}
