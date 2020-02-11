package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;


public class HRApiTestWithParameters {

    @BeforeClass
    public void setUpClass(){
        RestAssured.baseURI = ConfigurationReader.get("hrapi.uri");

    }

     /*
        Given accept type is Json
        And parameters: q = region_id=2
        When users sends a GET request to "/countries"
        Then status code is 200
        And Content type is application/json
        And Payload should contain "United States of America"
        {"region_id":2}
     */

    @Test
    public void countriesWithQueryParam(){
        Response response = given().accept(ContentType.JSON).and()
                .queryParam("q", "{\"region_id\":2}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.body().asString().contains("United States of America"));

    }

    //get the api request returns employees who are IT_PROG

    @Test
    public void employeesWithQueryParam(){
        Response response = given().accept(ContentType.JSON).and()
                .queryParam("q", "{\"job_id\":\"IT_PROG\"}")
                .when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertTrue(response.body().asString().contains("IT_PROG"));
        assertFalse(response.body().asString().contains("FI_ACCOUNT"));

    }
}
