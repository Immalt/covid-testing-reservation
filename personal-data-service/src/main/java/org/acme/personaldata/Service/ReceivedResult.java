package org.acme.personaldata.Service;

import org.acme.personaldata.Entity.ResultEntity;
import org.acme.personaldata.Message.ResultMessage;
import org.acme.personaldata.Message.ResultMessageEmail;
import org.acme.personaldata.RestClient.TimeSlotClient;
import org.eclipse.microprofile.reactive.messaging.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class ReceivedResult {
    @Inject
    @RestClient
    TimeSlotClient timeSlotClient;

    @Inject
    @Channel("result")
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 10000)
    Emitter<ResultMessageEmail> resultEmitter;

    public void sendMessage(ResultEntity result)
    {
        ResultMessageEmail out = new ResultMessageEmail();
        ResultMessage message = new ResultMessage();

        message.firstName = result.application.firstName;
        message.lastName = result.application.lastName;
        message.testType = result.testType;
        message.testResult = result.result;
        message.email = result.application.email;
        message.term = timeSlotClient.getReservation(result.application.id).term;
        out.emailData = message;

        resultEmitter.send(out);
    }
}
