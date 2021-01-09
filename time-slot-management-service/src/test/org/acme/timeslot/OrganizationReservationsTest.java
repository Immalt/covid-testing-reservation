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
public class OrganizationReservationsTest {
    @AfterEach
    public void cleanAll()
    {
        Reservation.deleteAll();
        WorkingDay.deleteAll();
        Organization.deleteAll();
    }

    public static void create(Organization organization, Reservation reservation) {
        JsonObject json = new JsonObject();
        json.put("applicationId", reservation.applicationId.toString());
        json.put("term", reservation.term.toString());


        Integer id = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/organization/" + organization.id + "/reservation" )
                .then()
                .statusCode(200)
                .body("applicationId", equalTo(reservation.applicationId.toString()))
                .body("term", equalTo(reservation.term.toString()))
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
        JsonObject json = new JsonObject();
        json.put("applicationId", reservation.applicationId.toString());
        json.put("term", reservation.term.toString());

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/organization/" + organization.id + "/reservation/" + reservation.id)
                .then()
                .statusCode(statusCode);
        if (statusCode.equals(200))
            response.contentType(ContentType.JSON)
                    .body("applicationId", equalTo(reservation.applicationId.toString()))
                    .body("term", equalTo(reservation.term.toString()));
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

        create(organization, reservation);

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

        update(organization, reservation);
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
    }
}
