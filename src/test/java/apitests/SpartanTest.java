package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class SpartanTest {

    String spartanAllUrl = "http://54.145.112.194:8000/api/spartans";

    @Test
    public void viewAllSpartans(){

        Response response = get(spartanAllUrl);

        //print the status code
        System.out.println(response.statusCode());

        //print response body
        //System.out.println(response.body().asString());

        //pretty print
        System.out.println(response.body().prettyPrint());

    }

    /*
    When user GET request to /api/spartans end point
    Then status code must be 200
    And body should contains Orion
     */

    @Test
    public void viewSpartanTest1(){

        Response response = get(spartanAllUrl);

        //verify status code is 200
        assertEquals(response.statusCode(),200);

        //verify body includes Orion
        assertTrue(response.body().asString().contains("Orion"));

    }

    /*
    Given accept type application/json
    When user GET request to /api/spartans end point
    Then status code must be 200
    And Response Content type must be Json
    And body should contains Orion
     */

    @Test
    public void viewSpartanTest2(){

        Response response = given().accept(ContentType.JSON).when().get(spartanAllUrl);

        //verify status code is 200
        assertEquals(response.statusCode(),200);

        //verify content type
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

    }

     /*
    Given accept type application/xml
    When user GET request to /api/spartans end point
    Then status code must be 200
    And Response Content type must be Xml
    And body should contains Orion
     */

    @Test
    public void viewSpartanTest3(){

        Response response = given().accept(ContentType.XML).when().get(spartanAllUrl);

        //verify status code
        assertEquals(response.statusCode(),200);

        //verify content type
        assertEquals(response.contentType(),"application/xml;charset=UTF-8");

        //verify body includes Orion
        assertTrue(response.body().asString().contains("Orion"));

    }

     /*
    Given accept type application/xml
    When user GET request to /api/spartans end point
    Then status code must be 200
    And Response Content type must be Xml
    And body should contains Orion
     */

    @Test
    public void viewSpartanTest4(){

                //request starts here
                given().accept(ContentType.XML).
                when().get(spartanAllUrl).
                        //response starts here
                        then().statusCode(200).
                        and().contentType("application/xml;charset=UTF-8");

    }

    /*
        Given the accept type Json
        When I send get request to /api/spartans/3
        Then status code must be 200
        And Content type should be json
        and body should contains Fidole
     */

    @Test
    public void viewSpartanTest5(){

        Response response = RestAssured.given().accept(ContentType.JSON).when().get(spartanAllUrl + "/3");

        assertEquals(response.statusCode(),200);

        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        assertTrue(response.body().asString().contains("Fidole"));
    }

    /*
       Given the accept type XML
       When I send get request to /api/spartans/3
       Then status code must be 406
    */

    @Test
    public void viewSpartanTest6(){

        given().accept(ContentType.XML).when().get(spartanAllUrl + "/3").then().statusCode(406);
    }

}
