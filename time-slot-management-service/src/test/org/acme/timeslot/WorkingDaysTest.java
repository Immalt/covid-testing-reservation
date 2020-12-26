package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.json.JsonObject;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.WorkingDay;
import org.acme.timeslot.enums.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Stereotype;
import javax.transaction.Transactional;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.DayOfWeek;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@Transactional
@QuarkusTestResource(H2DatabaseTestResource.class)
public class WorkingDaysTest {
    @AfterEach
    public void cleanAll()
    {
        Organization.deleteAll();
    }

    public static void createWorkingDay(Organization organization, WorkingDay day) {
        JsonObject json = new JsonObject();
        json.put("testInterval", day.testInterval);
        json.put("timeUnit", day.timeUnit);
        json.put("day", day.day);
        json.put("workingFrom", day.workingFrom.toString());
        json.put("workingTill", day.workingTill.toString());

        Integer id = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/organization/" + organization.id + "/working-day")
                .then()
                .statusCode(200)
                .body("testInterval", equalTo(day.testInterval))
                .body("timeUnit", equalTo(day.timeUnit.toString()))
                .body("day", equalTo(day.day.toString()))
                .body("workingTill", equalTo(day.workingTill.toString()))
                .body("workingFrom", equalTo(day.workingFrom.toString()))
                .contentType(ContentType.JSON).extract().path("id");
        day.id = Long.valueOf(id);
    }

    private static void deleteWorkingDay(Organization organization, WorkingDay day)
    {
        deleteWorkingDay(organization, day, 200);
    }

    private static void deleteWorkingDay(Organization organization, WorkingDay day, Integer statusCode)
    {
        given().contentType(ContentType.JSON)
                .when().delete("/organization/" + organization.id + "/working-day/" + day.id)
                .then()
                .statusCode(statusCode);
    }

    private static void updateWorkingDay(Organization organization, WorkingDay day)
    {
        updateWorkingDay(organization, day,200);
    }

    private static void updateWorkingDay(Organization organization, WorkingDay day, Integer statusCode)
    {
        JsonObject json = new JsonObject();
        json.put("testInterval", day.testInterval);
        json.put("timeUnit", day.timeUnit);
        json.put("day", day.day);
        json.put("workingFrom", day.workingFrom.toString());
        json.put("workingTill", day.workingTill.toString());

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/organization/" + organization.id + "/working-day/" + day.id)
                .then()
                .statusCode(statusCode);
        if (statusCode.equals(200))
            response
                    .body("testInterval", equalTo(day.testInterval))
                    .body("timeUnit", equalTo(day.timeUnit.toString()))
                    .body("day", equalTo(day.day.toString()))
                    .body("workingTill", equalTo(day.workingTill.toString()))
                    .body("workingFrom", equalTo(day.workingFrom.toString()));
    }

    @Test
    public void testListAllApplicationsEmpty() {
        Organization organization = new Organization();
        organization.organizationName = "Org";
        OrganizationTest.createOrganization(organization);
        given()
                .when().get("/organization/" + organization.id + "/working-day")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));


        given()
                .when().get("/organization/" + organization.id)
                .then()
                .statusCode(200)
                .body("workingDays.size()", is(0))
        ;
    }

    @Test
    public void testCreateUpdateDelete() {
        Organization organization = new Organization();
        organization.organizationName = "Org";
        OrganizationTest.createOrganization(organization);

        WorkingDay day = new WorkingDay();
        day.testInterval = 1;
        day.timeUnit = TimeUnit.HOURS;
        day.workingFrom = LocalTime.of(7,0);
        day.workingTill = LocalTime.of(17,0);
        day.day = DayOfWeek.MONDAY;

        createWorkingDay(organization, day);

        given()
                .when().get("/organization/" + organization.id)
                .then()
                .statusCode(200)
                .body("workingDays.size()", is(1));
        given()
                .when().get("/organization/" + organization.id + "/working-day")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        day.day = DayOfWeek.FRIDAY;
        day.workingFrom = LocalTime.of(8, 0);
        day.workingTill = LocalTime.of(18,0);
        day.testInterval = 2;

        updateWorkingDay(organization, day);
        deleteWorkingDay(organization, day);
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
