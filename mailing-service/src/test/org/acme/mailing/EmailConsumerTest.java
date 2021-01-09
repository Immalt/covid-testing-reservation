package org.acme.mailing;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.acme.mailing.DTO.*;
import org.acme.mailing.Enum.EmailType;
import org.acme.mailing.Enum.TestResult;
import org.acme.mailing.Enum.TestType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@QuarkusTest
@ApplicationScoped
@QuarkusTestResource(FakeKafkaResource.class)
public class EmailConsumerTest {
    @Inject
    @Any
    InMemoryConnector inMemoryConnector;

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        mailbox.clear();
    }
    @AfterEach
    void clean() {
        InMemoryConnector.clear();
    }

    @Test
    public void testResult()
    {
        ResultEmail resultEmail = new ResultEmail();
        Result result = new Result();
        result.email = "something@example.cz";
        result.testResult = TestResult.POSITIVE;
        result.testType = TestType.ANTIGEN;
        result.firstName = "Tom";
        result.lastName = "Jerry";
        result.term = LocalDateTime.now();
        resultEmail.emailData = result;

        InMemorySource<ResultEmail> results = inMemoryConnector.source("result");
        results.send(resultEmail);

        List<Mail> sent = mailbox.getMessagesSentTo(result.email);
        Assertions.assertEquals(1, sent.size());
        Mail actual = sent.get(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm:ss");
        Assertions.assertEquals(EmailType.RESULTS.subject(), actual.getSubject());
        Assertions.assertTrue(actual.getHtml().contains(result.firstName));
        Assertions.assertTrue(actual.getHtml().contains(result.lastName));
        Assertions.assertTrue(actual.getHtml().contains(result.term.format(formatter)));
        Assertions.assertTrue(actual.getHtml().contains(result.testResult.toString()));
        Assertions.assertTrue(actual.getHtml().contains(result.testType.toString()));
    }

    @Test
    public void testNewReservation()
    {
        ReservationEmail reservationEmail = new ReservationEmail();
        reservationEmail.emailType = EmailType.CREATED_RESERVATION;
        NewReservation reservation = new NewReservation();
        reservation.email = "something@example.cz";
        reservation.IDCardNumber = "4565522112";
        reservation.cancellationLink = "http://localhost/reservation";
        reservation.applicationId = UUID.randomUUID();
        reservation.firstName = "Tom";
        reservation.lastName = "Jerry";
        reservation.term = LocalDateTime.now();
        reservationEmail.emailData = reservation;

        InMemorySource<ReservationEmail> results = inMemoryConnector.source("reservation");
        results.send(reservationEmail);

        List<Mail> sent = mailbox.getMessagesSentTo(reservation.email);
        Assertions.assertEquals(1, sent.size());
        Mail actual = sent.get(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm:ss");
        Assertions.assertEquals(EmailType.CREATED_RESERVATION.subject(), actual.getSubject());
        Assertions.assertTrue(actual.getHtml().contains(reservation.firstName));
        Assertions.assertTrue(actual.getHtml().contains(reservation.lastName));
        Assertions.assertTrue(actual.getHtml().contains(reservation.term.format(formatter)));
        Assertions.assertTrue(actual.getHtml().contains(reservation.IDCardNumber));
        Assertions.assertTrue(actual.getHtml().contains(reservation.cancellationLink + "/" + reservation.applicationId));
    }

    @Test
    public void testCancelReservation()
    {
        ReservationEmail reservationEmail = new ReservationEmail();
        reservationEmail.emailType = EmailType.CANCELLED_RESERVATION;
        Reservation reservation = new Reservation();
        reservation.email = "something@example.cz";
        reservation.IDCardNumber = "4565522112";
        reservation.applicationId = UUID.randomUUID();
        reservation.firstName = "Tom";
        reservation.lastName = "Jerry";
        reservation.term = LocalDateTime.now();
        reservationEmail.emailData = reservation;

        InMemorySource<ReservationEmail> results = inMemoryConnector.source("reservation");
        results.send(reservationEmail);

        List<Mail> sent = mailbox.getMessagesSentTo(reservation.email);
        Assertions.assertEquals(1, sent.size());
        Mail actual = sent.get(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy HH:mm:ss");
        Assertions.assertEquals(EmailType.CANCELLED_RESERVATION.subject(), actual.getSubject());
        Assertions.assertTrue(actual.getHtml().contains(reservation.firstName));
        Assertions.assertTrue(actual.getHtml().contains(reservation.lastName));
        Assertions.assertTrue(actual.getHtml().contains(reservation.term.format(formatter)));
        Assertions.assertTrue(actual.getHtml().contains(reservation.IDCardNumber));
    }
}
