package org.acme.timeslot;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.vertx.core.json.JsonObject;
import org.acme.timeslot.entity.Organization;
import org.acme.timeslot.entity.Reservation;
import org.acme.timeslot.entity.WorkingDay;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@Transactional
@QuarkusTestResource(H2DatabaseTestResource.class)
public class OrganizationTest {
    @AfterEach
    @BeforeEach
    public void cleanAll()
    {
        Reservation.deleteAll();
        WorkingDay.deleteAll();
        Organization.deleteAll();
    }

    public static void createOrganization(Organization organization) {
        JsonObject json = new JsonObject();
        json.put("organizationName", organization.organizationName);

        Integer id = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/organization")
                .then()
                .statusCode(200)
                .body("organizationName", equalTo(organization.organizationName))
                .body("workingDays.size()", equalTo(0))
                .contentType(ContentType.JSON).extract().path("id");
        organization.id = Long.valueOf(id);
    }

    public static void delete(Organization organization)
    {
        delete(organization, 200);
    }

    private static void delete(Organization organization, Integer statusCode)
    {
        given().contentType(ContentType.JSON)
                .when().delete("/organization/" + organization.id)
                .then()
                .statusCode(statusCode);
    }

    private static void update(Organization organization)
    {
        update(organization, 200);
    }

    private static void update(Organization organization, Integer statusCode)
    {
        JsonObject json = new JsonObject();
        json.put("organizationName", organization.organizationName);

        ValidatableResponse response = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().put("/organization/" + organization.id)
                .then()
                .statusCode(statusCode);
        if (statusCode.equals(200))
                response.body("organizationName", equalTo(organization.organizationName))
                .body("workingDays", equalTo(organization.workingDays));
    }

    @Test
    public void testListAllApplicationsEmpty() {
        given()
                .when().get("/organization")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));
    }

    @Test
    public void testCreateAndDeleteUpdate() {
        Organization organization = new Organization();
        organization.organizationName = "some name";
        createOrganization(organization);

        organization.organizationName = "other name";
        update(organization);

        Organization notExisting = new Organization();
        notExisting.organizationName = "something";
        notExisting.id = -1L;
        update(notExisting, 404);
        given()
                .when().get("/organization")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].organizationName", equalTo(organization.organizationName))
        ;

        delete(notExisting, 404);
        delete(organization);
        assert null == Organization.findById(organization.id);
        given()
                .when().get("/organization")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));
    }
}
