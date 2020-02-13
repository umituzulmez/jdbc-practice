package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.sql.SQLException;
import java.util.List;

import static org.testng.Assert.*;
import static io.restassured.RestAssured.*;

public class SpartanHomework {

    @BeforeClass
    public void setUpClass(){
        RestAssured.baseURI = ConfigurationReader.get("spartanapi.uri");
    }

    /*
    Given accept type is json
    And path param id is 20
    When user sends a get request to "/spartans/{id}"
    Then status code is 200
    And content-type is "application/json;char"
    And response header contains Date
    And Transfer-Encoding is chunked
    And response payload values match the following:
        id is 20,
        name is "Lothario",
        gender is "Male",
        phone is 7551551687
     */

    @Test
    public void Q1(){

        Response response = given().accept(ContentType.JSON).
                and().pathParam("id","20").
                when().get("/spartans/{id}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");
        assertTrue(response.headers().hasHeaderWithName("Date"));
        assertEquals(response.header("Transfer-Encoding"),"chunked");

        int id = response.body().path("id");
        assertEquals(id,20);

        String name = response.body().path("name");
        assertEquals(name,"Lothario");

        String gender = response.body().path("gender");
        assertEquals(gender,"Male");

        long phone = response.body().path("phone");
        assertEquals(phone,7551551687L);

    }

    /*
    Given accept type is json
    And query param gender = Female
    And queary param nameContains = r
    When user sends a get request to "/spartans/search"
    Then status code is 200
    And content-type is "application/json;char"
    And all genders are Female
    And all names contains r
    And size is 20
    And totalPages is 1
    And sorted is false
    */

    @Test
    public void Q2() throws SQLException {

        Response response = given().accept(ContentType.JSON)
                .and().queryParam("gender","Female")
                .and().queryParam("nameContains","r")
                .when().get("/spartans/search");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        List<String> genders = response.path("content.gender");

        for (String gender : genders) {
            assertEquals(gender, "Female");
            //System.out.println("gender = " + gender);
        }

        List<String> names = response.body().path("content.name");

        for (String name : names) {
            assertTrue(name.toLowerCase().contains("r"));
            //System.out.println("name = " + name);
        }

        int size = response.body().path("size");
        assertEquals(size,20);

        int totalPage = response.body().path("totalPages");
        assertEquals(totalPage,1);

        boolean sorted = response.body().path("sort.sorted");
        assertEquals(sorted,false);
    }
}
