package org.acme.timeslot.service;

import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.enums.MessageEmailType;
import org.acme.timeslot.message.ReservationEmailMessage;
import org.acme.timeslot.message.ReservationMessage;
import org.acme.timeslot.restClient.PersonalDataClient;
import org.acme.timeslot.restClientModel.Application;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ReservationEvent {
    @Inject
    @RestClient
    PersonalDataClient personalDataClient;

    @Inject
    @Channel("reservation")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
    Emitter<ReservationEmailMessage> emitter;
    public void creationMessage(Reservation reservation)
    {
        sendMessage(reservation, MessageEmailType.CREATED_RESERVATION);
    }

    public void deletionMessage(Reservation reservation)
    {
        sendMessage(reservation, MessageEmailType.CANCELLED_RESERVATION);
    }

    public void sendMessage(Reservation reservation, MessageEmailType emailType)
    {
        ReservationEmailMessage out = new ReservationEmailMessage();
        out.emailType = emailType;
        ReservationMessage message = new ReservationMessage();

        Application application = personalDataClient.getApplication(reservation.applicationId);

        message.firstName = application.firstName;
        message.lastName = application.lastName;
        message.IDCardNumber = application.idCardNumber;
        message.applicationId = reservation.applicationId;
        message.email = application.email;
        message.term = reservation.term;
        out.emailData = message;

        emitter.send(out);
    }
}
