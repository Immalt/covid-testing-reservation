package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.json.JsonObject;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.entity.WorkingDay;
import org.acme.timeslot.enums.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Transactional
@QuarkusTestResource(H2DatabaseTestResource.class)
public class ReservationsTest {
    @AfterEach
    public void cleanAll()
    {
        Reservation.deleteAll();
        WorkingDay.deleteAll();
        Organization.deleteAll();
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

        OrganizationReservationsTest.create(organization, reservation);

        given().contentType(ContentType.JSON)
                .queryParam("applicationId", reservation.applicationId)
                .get("/reservation")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("applicationId", equalTo(reservation.applicationId.toString()))
                .body("term", equalTo(reservation.term.toString()));
    }
}
