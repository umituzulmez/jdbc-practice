package uinamestests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;

public class uiNamesApiTests {

    @BeforeClass
    public void setUp(){
        baseURI = "https://uinames.com/api/";
    }

    /*
    1. Send a get request without providing any parameters
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that name, surname, gender, region fields have value
     */

    @Test
    public void noParamsTest(){

        Response response = given().get(baseURI);

        assertEquals(response.statusCode(),200);

        String name = response.body().path("name");
        assertFalse(name.isEmpty());
        String surname = response.body().path("surname");
        assertFalse(surname.isEmpty());
        String gender = response.body().path("gender");
        assertFalse(gender.isEmpty());
        String region = response.body().path("region");
        assertFalse(region.isEmpty());

        assertFalse(response.body().asString().isEmpty());
        System.out.println("response = " + response.body().asString());
    }

    /*
    1. Create a request by providing query parameter: gender, male or female
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that value of gender field is same from step 1
    */

    @Test
    public void genderTest(){

        Response response = given().queryParam("gender","male")
                            .when().get();

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");
        assertEquals(jsonPath.getString("gender"),"male");
    }

    /*
    1. Create a request by providing query parameters: a valid region and gender
    NOTE: Available region values are given in the documentation
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that value of gender field is same from step 1
    4. Verify that value of region field is same from step 1
     */

    @Test
    public void paramsTest() {

        Response response = given().queryParam("gender", "female")
                .and().queryParam("region", "Turkey")
                .when().get(baseURI);

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");
        assertEquals(jsonPath.getString("gender"),"female");
        assertEquals(jsonPath.getString("region"),"Turkey");
    }

    /*
    1. Create a request by providing query parameter: invalid gender
    2. Verify status code 400 and status line contains Bad Request
    3. Verify that value of error field is Invalid gender
     */

    @Test
    public void invalidGenderTest(){

        Response response = given().queryParam("gender","smthng")
                            .when().get();

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),400);
        assertTrue(response.statusLine().contains("Bad Request"));
        System.out.println("Status Line = " + response.statusLine());

        assertEquals(jsonPath.getString("error"),"Invalid gender");
        System.out.println("response body " + jsonPath.getString("error"));
    }

    /*
    1. Create a request by providing query parameter: invalid region
    2. Verify status code 400 and status line contains Bad Request
    3. Verify that value of error field is Region or language not found
     */

    @Test
    public void invalidRegionTest(){

        Response response = given().queryParam("region","Sakarya")
                            .when().get(baseURI);

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),400);
        assertTrue(response.statusLine().contains("Bad Request"));

        assertEquals(jsonPath.getString("error"),"Region or language not found");
    }

    /*
    1. Create request by providing query parameters: a valid region and amount (must be bigger than 1)
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that all objects have different name+surname combination
     */

    @Test
    public void amountAndRegionTest(){

        Response response = given().queryParam("region","Turkey")
                            .and().queryParam("amount","11")
                            .when().get(baseURI);

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");

        List<Map<String,String>> fullNamesMapList = response.body().as(List.class);
        System.out.println("fullNamesList = " + fullNamesMapList);

        int counter = 0;
        for(int i=0; i<fullNamesMapList.size(); i++){
            counter = 0;
            for(int j=0; j<fullNamesMapList.size(); j++){
                if((fullNamesMapList.get(i).get("name") + fullNamesMapList.get(i).get("surname"))
                        .equals(fullNamesMapList.get(j).get("name") + fullNamesMapList.get(j).get("surname"))){
                    counter++;
                }
            }
            if(counter>=2){
                break;
            }
            System.out.println(fullNamesMapList.get(i).get("name") + fullNamesMapList.get(i).get("surname"));
        }

        assertTrue(counter<2);
        System.out.println("counter = " + counter);
    }

    /*
    1. Create a request by providing query parameters: a valid region, gender and amount (must be bigger than 1)
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that all objects the response have the same region and gender passed in step 1
     */

    @Test
    public void threeParamsTest(){

        Response response = given().queryParam("region","Turkey")
                            .and().queryParam("gender","male")
                            .and().queryParam("amount", 5)
                            .when().get(baseURI);

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");

        List<String> regionList = response.body().path("region");
        System.out.println("regionList = " + regionList);

        for (String s : regionList) {
            assertEquals(s,"Turkey");
        }

        List<String> genderList = response.body().path("gender");
        System.out.println("genderList = " + genderList);

        for (String s : genderList) {
            assertEquals(s,"male");
        }
    }

    /*
    1. Create a request by providing query parameter: amount (must be bigger than 1)
    2. Verify status code 200, content type application/json; charset=utf-8
    3. Verify that number of objects returned in the response is same as the amount passed in step 1
     */

    @Test
    public void amountCountTest(){

        Response response = given().queryParam("amount", 9)
                            .when().get(baseURI);

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json; charset=utf-8");
        assertEquals(response.body().as(List.class).size(),9);
    }
}
