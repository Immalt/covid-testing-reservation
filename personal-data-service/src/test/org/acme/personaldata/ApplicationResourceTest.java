package org.acme.personaldata;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.restassured.http.ContentType;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import javax.transaction.Transactional;

@QuarkusTest
@Transactional
@QuarkusTestResource(H2DatabaseTestResource.class)
public class ApplicationResourceTest {
    @AfterEach
    public void clean()
    {
        ApplicationEntity.deleteAll();
    }

    @Test
    public void testListAllApplicationsEmpty() {
        given()
                .when().get("/applications")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));
    }


    @Test
    public void testListAllApplicationsWithManipulation() {
        given()
                .when().get("/applications")
                .then()
                .statusCode(200)
                .body("size()", is(0))
                .body(is("[]"));

        JsonObject json = new JsonObject();
        json.put("idCardNumber", "IDCard123");
        json.put("firstName", "Tom");
        json.put("lastName", "Jerry");
        json.put("medicalInsurance", "HealthInstitute");

        String id = given().contentType(ContentType.JSON)
                .body(json.toString())
                .when().post("/applications")
                .then()
                .statusCode(200)
                .body("idCardNumber", equalTo("IDCard123"))
                .body("firstName", equalTo("Tom"))
                .body("lastName", equalTo("Jerry"))
                .body("medicalInsurance", equalTo("HealthInstitute"))
                .contentType(ContentType.JSON).extract().path("id")
        ;

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + id)
                .then()
                .statusCode(200)
                .body("idCardNumber", equalTo("IDCard123"))
                .body("firstName", equalTo("Tom"))
                .body("lastName", equalTo("Jerry"))
                .body("medicalInsurance", equalTo("HealthInstitute"))
                .body("id", equalTo(id));

        given().contentType(ContentType.JSON)
                .when().get("/applications").then().statusCode(200)
                .assertThat()
                .body("size()", is(1))
                .body("[0].idCardNumber", equalTo("IDCard123"))
                .body("[0].firstName", equalTo("Tom"))
                .body("[0].lastName", equalTo("Jerry"))
                .body("[0].medicalInsurance", equalTo("HealthInstitute"))
                .body("[0].id", equalTo(id));

        given().contentType(ContentType.JSON)
                .when().delete("/applications/" + id)
                .then()
                .statusCode(200);

        given().contentType(ContentType.JSON)
                .when().get("/applications").then().statusCode(200)
                .assertThat()
                .body("size()", is(0))
                .body(is("[]"));

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    public void testResults() {
        JsonObject jsonApplication = new JsonObject();
        jsonApplication.put("idCardNumber", "IDCard123");
        jsonApplication.put("firstName", "Tom");
        jsonApplication.put("lastName", "Jerry");
        jsonApplication.put("medicalInsurance", "HealthInstitute");

        String applicationId = given().contentType(ContentType.JSON)
                .body(jsonApplication.toString())
                .when().post("/applications")
                .then()
                .statusCode(200)
                .body("idCardNumber", equalTo("IDCard123"))
                .body("firstName", equalTo("Tom"))
                .body("lastName", equalTo("Jerry"))
                .body("medicalInsurance", equalTo("HealthInstitute"))
                .contentType(ContentType.JSON).extract().path("id")
                ;

        JsonObject resultApplication1 = new JsonObject();
        resultApplication1.put("result", "NEGATIVE");
        resultApplication1.put("testType", "ANTIGEN");

        Integer resultId1 = given().contentType(ContentType.JSON)
                .body(resultApplication1.toString())
                .when().post("/applications/" + applicationId + "/results")
                .then()
                .statusCode(200)
                .body("result", equalTo("NEGATIVE"))
                .body("testType", equalTo("ANTIGEN"))
                .contentType(ContentType.JSON).extract().path("id")
                ;

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results/" + resultId1)
                .then().statusCode(200)
                .assertThat()
                .body("result", equalTo("NEGATIVE"))
                .body("testType", equalTo("ANTIGEN"))
                .body("id", equalTo(resultId1));

        JsonObject resultApplication2 = new JsonObject();
        resultApplication2.put("result", "POSITIVE");
        resultApplication2.put("testType", "PCR");

        Integer resultId2 = given().contentType(ContentType.JSON)
                .body(resultApplication2.toString())
                .when().post("/applications/" + applicationId + "/results")
                .then()
                .statusCode(200)
                .body("result", equalTo("POSITIVE"))
                .body("testType", equalTo("PCR"))
                .contentType(ContentType.JSON).extract().path("id");

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results/" + resultId2)
                .then().statusCode(200)
                .assertThat()
                .body("result", equalTo("POSITIVE"))
                .body("testType", equalTo("PCR"))
                .body("id", equalTo(resultId2));

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results")
                .then().statusCode(200)
                .assertThat()
                .body("size()", is(2));


        given().contentType(ContentType.JSON)
                .when().delete("/applications/" + applicationId + "/results/" + resultId1)
                .then()
                .statusCode(200);

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results")
                .then().statusCode(200)
                .assertThat()
                .body("size()", is(1));

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results/" + resultId1)
                .then()
                .statusCode(204);

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results/" + resultId2)
                .then()
                .statusCode(200);

        given().contentType(ContentType.JSON)
                .when().delete("/applications/" + applicationId)
                .then()
                .statusCode(200);

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId)
                .then()
                .statusCode(204);

        given().contentType(ContentType.JSON)
                .when().get("/applications/" + applicationId + "/results/" + resultId2)
                .then()
                .statusCode(204);
    }
}
