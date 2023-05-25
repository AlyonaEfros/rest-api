package test;

import models.ErrorResponse;
import models.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;



import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;

public class RequestApiTest {

    @Test
    void successfulCreateTest() {

        given()
                .spec(request)
                .when()
                .post("/users")
                .then()
                .spec(responseCreated)
                .log().body();
    }

    @ValueSource(strings = {"email", "password"})
    @ParameterizedTest(name = "Unsuccessful user registration: missing parameter {0}")
    public void registerWithoutOneParam(String parameter) {
        String email = "eve.holt@reqres.in";
        String password = "12234";
        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);

        data.remove(parameter);

        ErrorResponse registerError =
                given()
                        .spec(request)
                        .body(data)
                        .when()
                        .post("/register/")
                        .then()
                        .spec(responseBadRequest)
                        .log().body()
                        .extract().as(ErrorResponse.class);

        assertThat(registerError.getError(), containsString("Missing " + parameter));
    }


    @Test
    void singleUserTest() {

        UserData data = given()
                .spec(request)
                .when()
                .get("/users/2")
                .then()
                .spec(responseOk)
                .log().body()
                .extract().as(UserData.class);

        assertEquals("janet.weaver@reqres.in", data.getUser().getEmail());
    }

    @Test
    void listResourceTest() {

        given()
                .spec(request)
                .when()
                .get("/unknown")
                .then()
                .spec(responseOk)
                .body("data.color[3]",
                        equalTo("#7BC4C4"))
                .and()
                .body("data.findAll{it.name =~/./}.name.flatten()",
                        hasItem("aqua sky"))
                .and()
                .body("data.pantone_value.flatten()",
                        hasItems("15-4020", "17-2031", "19-1664", "14-4811", "17-1456", "15-5217"));
    }


}
