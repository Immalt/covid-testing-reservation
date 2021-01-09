package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.vertx.core.json.JsonObject;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.entity.WorkingDay;
import org.acme.timeslot.enums.MessageEmailType;
import org.acme.timeslot.enums.TimeUnit;
import org.acme.timeslot.message.ReservationEmailMessage;
import org.acme.timeslot.restClient.PersonalDataClient;
import org.acme.timeslot.restClientModel.Application;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Transactional
@ApplicationScoped
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(FakeKafkaResource.class)
public class OrganizationReservationsTest {
    @InjectMock
    @RestClient
    public PersonalDataClient personalDataClient;

    @Inject
    @Any
    public InMemoryConnector connector;

    @AfterEach
    @BeforeEach
    public void cleanAll()
    {
        Reservation.deleteAll();
        WorkingDay.deleteAll();
        Organization.deleteAll();
        connector.sink("reservation").clear();
    }

    public static void create(Organization organization, Reservation reservation) {
        JsonObject json = new JsonObject();
        json.put("applicationId", reservation.applicationId.toString());
        json.put("term", reservation.term.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        String time = reservation.term.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Integer id = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/organization/" + organization.id + "/reservation" )
                .then()
                .statusCode(200)
                .body("applicationId", equalTo(reservation.applicationId.toString()))
                .body("term", equalTo(time))
                .contentType(ContentType.JSON).extract().path("id");
        reservation.id = Long.valueOf(id);
    }

    public static void delete(Organization organization, Reservation reservation)
    {
        delete(organization, reservation, 200);
    }

    private static void delete(Organization organization, Reservation reservation, Integer statusCode)
    {
        given().contentType(ContentType.JSON)
                .when().delete("/organization/" + organization.id + "/reservation/" + reservation.id)
                .then()
                .statusCode(statusCode);
    }

    private static void update(Organization organization, Reservation reservation)
    {
        update(organization, reservation,200);
    }

    private static void update(Organization organization, Reservation reservation, Integer statusCode)
    {
        String time = reservation.term.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        JsonObject json = new JsonObject();
        json.put("applicationId", reservation.applicationId.toString());
        json.put("term", time);

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/organization/" + organization.id + "/reservation/" + reservation.id)
                .then()
                .statusCode(statusCode);
        if (statusCode.equals(200))
            response.contentType(ContentType.JSON)
                    .body("applicationId", equalTo(reservation.applicationId.toString()))
                    .body("term", equalTo(time));
    }

    @Test
    public void testNewOrganizationHasNoReservation() {
        Organization organization = new Organization();
        organization.organizationName = "Org";
        OrganizationTest.createOrganization(organization);
        given()
                .when().get("/organization/" + organization.id + "/reservation")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));
    }

    @Test
    public void testCreateUpdateDelete() {
        Organization organization = new Organization();
        organization.organizationName = "Org";
        OrganizationTest.createOrganization(organization);

        Reservation reservation = new Reservation();
        reservation.applicationId = UUID.randomUUID();
        reservation.term = LocalDate.now().atTime(9, 0);

        WorkingDay day = new WorkingDay();
        day.testInterval = 1;
        day.timeUnit = TimeUnit.HOURS;
        day.workingFrom = LocalTime.of(7,0);
        day.workingTill = LocalTime.of(17,0);
        day.day = reservation.term.getDayOfWeek();
        WorkingDaysTest.createWorkingDay(organization, day);

        Application application = new Application();
        application.firstName = "Tom";
        application.lastName = "Jerry";
        application.email = "something@example.cz";
        application.idCardNumber = "552233446688cz";
        application.medicalInsurance = "OOO";
        Mockito.when(personalDataClient.getApplication(reservation.applicationId)).thenReturn(application);
        given()
                .when().get("/organization/" + organization.id + "/reservation/list-free")
                .then()
                .statusCode(200)
                .body("size()", is(10));
        create(organization, reservation);

        InMemorySink<ReservationEmailMessage> reservations = connector.sink("reservation");

        Assertions.assertEquals(1, reservations.received().size());
        ReservationEmailMessage message = reservations.received().get(0).getPayload();
        Assertions.assertEquals( MessageEmailType.CREATED_RESERVATION, message.emailType);
        Assertions.assertEquals(application.email, message.emailData.email);
        Assertions.assertEquals(application.firstName, message.emailData.firstName);
        Assertions.assertEquals(application.lastName, message.emailData.lastName);
        Assertions.assertEquals(reservation.term, message.emailData.term);
        Assertions.assertEquals(application.idCardNumber, message.emailData.IDCardNumber);

        given()
                .when().get("/organization/" + organization.id + "/reservation")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        reservation.applicationId = UUID.randomUUID();
        reservation.term = reservation.term.plusHours(3);


        given()
                .when().get("/organization/" + organization.id + "/reservation/list-free")
                .then()
                .statusCode(200)
                .body("size()", is(9));

        Timestamp from = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));
        Timestamp till = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

        given().contentType(ContentType.JSON)
                .queryParam("fromTimestamp", from.getTime())
                .queryParam("tillTimestamp", till.getTime())
                .when().get("/organization/" + organization.id + "/reservation/list-free")
                .then()
                .statusCode(200)
                .body("size()", is(9));

        update(organization, reservation);
        Mockito.when(personalDataClient.getApplication(reservation.applicationId)).thenReturn(application);
        delete(organization, reservation);

        OrganizationTest.delete(organization);
        given()
                .when().get("/organization/" + organization.id)
                .then()
                .statusCode(204);

        given()
                .when().get("/organization")
                .then()
                .statusCode(200).body("size()", is(0))
        ;


        Assertions.assertEquals(2, reservations.received().size());
        ReservationEmailMessage message1 = reservations.received().get(1).getPayload();
        Assertions.assertEquals( MessageEmailType.CANCELLED_RESERVATION, message1.emailType);
        Assertions.assertEquals(application.email, message1.emailData.email);
        Assertions.assertEquals(application.firstName, message1.emailData.firstName);
        Assertions.assertEquals(application.lastName, message1.emailData.lastName);
        Assertions.assertEquals(reservation.term, message1.emailData.term);
        Assertions.assertEquals(application.idCardNumber, message1.emailData.IDCardNumber);

    }
}
