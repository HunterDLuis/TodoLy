package basicRestAssured;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class CreateItemCheckTest {

    @Test
    public void verifyCreateItemSCHEMA(){

        JSONObject body = new JSONObject();
        body.put("Content","HunterJSON34");

        Response response =given()
                .auth()
                .preemptive()
                .basic("hunter1@gmail.com","diplo123")
                .body(body.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/items.json");


        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo("HunterJSON34"));

        // verificacion por schema
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder()
                .setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(
                        SchemaVersion.DRAFTV4
                ).freeze()).freeze();

        response.then()
                .body(matchesJsonSchemaInClasspath("schemaCreateItemResponse2.json")
                        .using(jsonSchemaFactory));

        int id=response.then().extract().path("Id");
        String nameProject= response.then().extract().path("Content");

        System.out.println("************* ID: "+id);
        System.out.println("************* NAME_ITEM: "+nameProject);

    }


}

