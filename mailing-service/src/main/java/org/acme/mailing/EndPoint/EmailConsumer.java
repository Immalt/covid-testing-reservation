package org.acme.mailing.EndPoint;

import org.acme.mailing.DTO.NewReservation;
import org.acme.mailing.DTO.Reservation;
import org.acme.mailing.DTO.ReservationEmail;
import org.acme.mailing.DTO.ResultEmail;
import org.acme.mailing.Enum.EmailType;
import org.acme.mailing.Enum.LinkType;
import org.acme.mailing.Exception.UnknownEmailType;
import org.acme.mailing.Exception.UnknownLinkType;
import org.acme.mailing.Factory.LinkFactory;
import org.acme.mailing.Factory.TemplateFactory;
import org.acme.mailing.Service.EmailService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.inject.Inject;

public class EmailConsumer {
    @Inject
    EmailService emailService;

    @Inject
    LinkFactory linkFactory;

    @Incoming("reservation")
    public void reservationConsumer(ReservationEmail reservation) throws UnknownEmailType, UnknownLinkType {
        if (reservation.emailType.equals(EmailType.CREATED_RESERVATION)) {
            reservation.emailData = new NewReservation(reservation.emailData, linkFactory.getLink(LinkType.CANCELLATION_LINK));
        }
        emailService.sendEmail(reservation.emailType, reservation.emailData);
    }

    @Incoming("result")
    public void resultConsumer(ResultEmail resultEmail) throws UnknownEmailType {
        emailService.sendEmail(resultEmail.emailType, resultEmail.emailData);
    }
}
