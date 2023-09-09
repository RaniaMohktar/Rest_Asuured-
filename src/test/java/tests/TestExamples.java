package tests;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

import data.data_driven;
import io.restassured.http.ContentType;

import static io.restassured.matcher.RestAssuredMatchers.*;

import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestExamples {
    @Test(enabled = false)
    public void test_1() {
        Response response = get("https://reqres.in/api/users?page=2");
        System.out.println(response.getStatusCode());
        System.out.println(response.getTime());
        System.out.println(response.getHeader("content-type"));
        System.out.println(response.getStatusLine());
        System.out.println(response.getBody().asString());
        int statuscode = response.getStatusCode();
        Assert.assertEquals(statuscode, 404);

    }

    /*important links
    https://jsonpathfinder.com/
    https://github.com/rest-assured/rest-assured/wiki/Usage
    https://www.youtube.com/watch?v=EvG8r7AhanI&list=PLhW3qG5bs-L8xPrBwDv66cTMlFNeUPdJx&index=6
    https://ecs.syr.edu/faculty/fawcett/Handouts/cse775/code/calcWebService/Calc.asmx?op=Add
    https://www.freeformatter.com/xsd-generator.html#before-output
    https://ecs.syr.edu/faculty/fawcett/Handouts/cse775/code/calcWebService/Calc.asmx
    * */

    @DataProvider
    public Object[][] data() throws IOException, InvalidFormatException {
        data_driven DD = new data_driven();
        return DD.read_data();
    }

    @Test(dataProvider = "data")
    public void test_get(String Fname) {
        baseURI = "https://reqres.in/api";
        given().
                get("/users?page=2").
                then().
                statusCode(200).
                body("data[1].first_name", equalTo(Fname));
          //body("data.first_name", hasItems("Lindsay", "Mihael"));
      //  body("data[1].id", equalTo(8));
    }


  //  @Test(enabled = false)
  @Test
  public void test_post() {
      baseURI = "https://reqres.in/api";
      JSONObject request = new JSONObject();
      request.put("name", "morpheus");
      request.put("job", "tester");

      given().
              body(request.toJSONString()).
              when().
              post("users").
                      then().
              statusCode(201);
  }
    @Test
    public void test_put() {
        baseURI = "https://reqres.in/api";
        JSONObject request = new JSONObject();
        request.put("name", "morpheus");
        request.put("job", "leader");

        given().
                body(request.toJSONString()).
                when().
                put("users/2").
                //the same in patch but we put the data which will have a change only
                        then().
                statusCode(200);
    }

    @Test(enabled = false)
    public void test_delete() {
        baseURI = "https://reqres.in/api";

        given().
                when().
                delete("/users/2").
                then().
                statusCode(204);
    }

    //important note:- schema file must be in path of target folder -> test classes
    @Test(enabled = false)
    public void json_validate() {

        baseURI = "https://reqres.in/api";
        given().
                get("/users?page=2").
                then().
                assertThat().body(matchesJsonSchemaInClasspath("schema.json")).
                statusCode(200);

    }


    @Test(enabled = false)
    public void test_soap() throws IOException {


        File file = new File("./SOAP_requests/add.xml");
        FileInputStream fileIS = new FileInputStream(file);
        String request_body = IOUtils.toString(fileIS, "UTF-8");


        baseURI = "https://ecs.syr.edu";
        given().contentType("text/xml").accept(ContentType.XML).body(request_body).when().
                post("/faculty/fawcett/Handouts/cse775/code/calcWebService/Calc.asmx").
                then().statusCode(200).log().all();


    }

    @Test(enabled = false)
    public void xml_validate() throws IOException {
        File file = new File("./SOAP_requests/add.xml");
        FileInputStream fileIS = new FileInputStream(file);
        String request_body = IOUtils.toString(fileIS, "UTF-8");


        baseURI = "https://ecs.syr.edu";
        given().contentType("text/xml").accept(ContentType.XML).body(request_body).when().
                post("/faculty/fawcett/Handouts/cse775/code/calcWebService/Calc.asmx").
                then().
                statusCode(200).log().all().
                assertThat().body(matchesXsdInClasspath("response.xsd"));

    }
}
