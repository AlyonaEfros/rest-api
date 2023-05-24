import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestApiTest {

    String baseUrl = "https://reqres.in";

    @Test
    void successfulCreateTest() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post(baseUrl + "/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void successfulRegisterTest() {
        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post(baseUrl + "/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unSuccessfulLoginWithMissingEmailTest() {
        String body = "{ \"password\": \"pistol\" }";

        given()
                .log().uri()
                .body(body)
                .contentType(JSON)
                .when()
                .post(baseUrl + "/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));

    }

    @Test
    void singleUserTest() {
        String expectedEmail = "janet.weaver@reqres.in";

        String actualEmail = given()
                .log().uri()
                .when()
                .get(baseUrl + "/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path( "data.email");

        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void listResourceTest() {
      List <String> expectedName = new ArrayList<>();
        expectedName.add("cerulean");
        expectedName.add("fuchsia rose");
        expectedName.add("true red");
        expectedName.add("aqua sky");
        expectedName.add("tigerlily");
        expectedName.add("blue turquoise");

        List <String> actualName = given()
                .log().uri()
                .when()
                .get(baseUrl + "/api/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().path( "data.name");

        assertEquals(expectedName, actualName);
    }


}
