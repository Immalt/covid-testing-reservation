package org.acme.personaldata;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.json.JsonObject;
import org.acme.personaldata.entity.Organization;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class OrganizationTest {
    private void createOrganization(Organization organization) {
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

    private void delete(Organization organization)
    {
        delete(organization, 200);
    }

    private void delete(Organization organization, Integer statusCode)
    {
        JsonObject json = new JsonObject();
        json.put("organizationName", organization.organizationName);

        given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().delete("/organization/" + organization.id)
                .then()
                .statusCode(statusCode);
    }

    private void update(Organization organization)
    {
        update(organization, 200);
    }

    private void update(Organization organization, Integer statusCode)
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
    }
}
