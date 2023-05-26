package test;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.Specs.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class RequestApiTest {


    @Test
    @DisplayName("Проверка запроса на создание пользователя")
    void successfulCreateTest() {
        User user = new User();
        user.setName("morpheus");
        user.setJob("leader");

        ResponseCreate response = step("Make request for create user", () ->
                given(request)
                        .body(user)
                        .when()
                        .post("/users")
                        .then()
                        .spec(responseCreated)
                        .extract().as(ResponseCreate.class));

        step("Verify expected name", () ->
                assertThat(response.getName()).isEqualTo("morpheus"));
        step("Verify expected job", () ->
                assertThat(response.getJob()).isEqualTo("leader"));

    }

    @Test
    @DisplayName("Проверка информации одного пользователя")
    void singleUserTest() {

        ResponseUser response = step("Checking one user's information", () ->
                given(request)
                        .when()
                        .get("/users/2")
                        .then()
                        .spec(responseOk)
                        .extract().as(ResponseUser.class));


        step("Verify expected email", () ->
                assertThat(response.getUser().getEmail()).isEqualTo("janet.weaver@reqres.in"));
    }

    @Test
    @DisplayName("Проверка запроса на успешное создание пользователя")
    void successfulRegisterTest() {
        User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("pistol");

        ResponseRegister response = step("Make request for create user", () ->
                given(request)
                        .body(user)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseRegister)
                        .extract().as(ResponseRegister.class));

        step("Verify expected id", () ->
                assertThat(response.getId()).isEqualTo(4));
    }

    @Test
    @DisplayName("Проверка запроса на отсутвие пароля создание пользователя")
    void unsuccessfulRegisterTest() {
        User user = new User();
        user.setEmail("eve.holt@reqres.in");
        user.setPassword("");

        ErrorResponse response = step("Make request for create user", () ->
                given(request)
                        .body(user)
                        .when()
                        .post("/register")
                        .then()
                        .spec(responseSpecStatusCodes400)
                        .extract().as(ErrorResponse.class));

        step("Verify error", () ->
                assertThat(response.getError()).isEqualTo("Missing password"));
    }

}
