package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.vertx.core.json.JsonObject;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.entity.WorkingDay;
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
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
@Transactional
@ApplicationScoped
@QuarkusTestResource(FakeKafkaResource.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class ReservationsTest {
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
        String time = reservation.term.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        JsonObject json = new JsonObject();
        json.put("applicationId", reservation.applicationId.toString());
        json.put("term", time);


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

    @Test
    public void testCreateUpdateDelete() {
        Organization organization = new Organization();
        organization.organizationName = "Org";
        OrganizationTest.createOrganization(organization);

        Reservation reservation = new Reservation();
        reservation.applicationId = UUID.randomUUID();
        reservation.term = LocalDate.now().atTime(9, 0, 0);

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
        create(organization, reservation);

        given().contentType(ContentType.JSON)
                .queryParam("applicationId", reservation.applicationId)
                .get("/reservation")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("applicationId", equalTo(reservation.applicationId.toString()))
                .body("term", equalTo(reservation.term.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        InMemorySink<ReservationEmailMessage> reservations = connector.sink("reservation");

        Assertions.assertEquals(1, reservations.received().size());
    }
}
