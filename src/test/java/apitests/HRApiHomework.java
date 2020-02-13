package apitests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;
import static io.restassured.RestAssured.*;

public class HRApiHomework {

    @BeforeClass
    public void setUpClass(){
        RestAssured.baseURI = ConfigurationReader.get("hrapi.uri");
    }

    /*
    - Given accept type is Json
    - Path param value- US
    - When users sends request to /countries
    - Then status code is 200
    - And Content - Type is Json
    - And country_id is US
    - And Country_name is United States of America
    - And Region_id is 2
     */

    @Test
    public void Q1(){

        Response response = given().accept(ContentType.JSON).
                and().pathParam("country_id","US").
                when().get("/countries/{country_id}");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        String countryID = response.path("country_id");
        assertEquals(countryID,"US");

        String countryName = response.path("country_name");
        assertEquals(countryName,"United States of America");

        int regoinID = response.path("region_id");
        assertEquals(regoinID,2);

    }

    /*
    - Given accept type is Json
    - Query param value - q={"department_id":80}
    - When users sends request to /employees
    - Then status code is 200
    - And Content - Type is Json
    - And all job_ids start with 'SA'
    - And all department_ids are 80
    - Count is 25
     */


    @Test
    public void Q2(){

        Response response = given().accept(ContentType.JSON).
                and().queryParam("q","{\"department_id\":80}").
                when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        List<String> jobIDs = response.body().path("items.job_id");

        for (String jobID : jobIDs) {
            assertTrue(jobID.startsWith("SA"));
            //System.out.println("jobID = " + jobID);
        }

        List<Object> departmentIDs = response.path("items.department_id");

        for (Object departmentID : departmentIDs) {
            assertEquals(departmentID,80);
            //System.out.println("departmentID = " + departmentID);
        }

        int count = response.path("count");
        assertEquals(count,25);
    }

    /*
    - Given accept type is Json
    - Query param value q= region_id 3
    - When users sends request to /countries
    - Then status code is 200
    - And all regions_id is 3
    - And count is 6
    - And hasMore is false
    - And Country_name are;
    - Australia,China,India,Japan,Malaysia,Singapore
     */

    @Test
    public void Q3(){

        Response response = RestAssured.given().accept(ContentType.JSON).
                and().queryParam("q","{\"region_id\":3}").
                when().get("/countries");

        assertEquals(response.statusCode(),200);

        List<Object> regionsIDs = response.path("items.region_id");

        for (Object regionsID : regionsIDs) {
            assertEquals(regionsID,3);
            //System.out.println("regionsID = " + regionsID);
        }

        int count = response.path("count");
        assertEquals(count,6);

        boolean hasMore = response.path("hasMore");
        assertEquals(hasMore,false);

        List<String> expectedCountryNames = Arrays.asList("Australia","China","India","Japan","Malaysia","Singapore");
        List<String> actualCountryNames = response.path("items.country_name");
        assertEquals(expectedCountryNames,actualCountryNames);
    }

}
