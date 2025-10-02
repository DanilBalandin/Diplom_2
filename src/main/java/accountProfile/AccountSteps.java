package accountProfile;

import constants.Url;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Endpoints.AccountEndpoint.*;
import static io.restassured.RestAssured.given;

//URL лежит отдельно, чтобы ее не писать везде
public class AccountSteps extends Url {


    // Тут регаем нового пользователя
    @Step("Создание нового пользователя")
    @Description("Будем передавать данные для регистрации")
    public Response createNewUser(Account userData) {
        setUrl();
        return given()
                .header("Content-Type", "application/json")
                .body(userData)
                .post(REGISTER_ACCOUNT);
    }

    // Тут логинимся
    @Step("Аутентификация")
    @Description("Будем передавать данные для авторизации")
    public Response authenticate(Account userData) {
        setUrl();
        return given()
                .header("Content-type", "application/json")
                .body(userData)
                .when()
                .post(AUTHENTICATE_ACCOUNT);
    }
    // Обновляем данные
    @Step("Изменения в аккаунте")
    @Description("Обновляем данные")
    public Response accountUpdate(String accessToken, Account userData) {
        setUrl();
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(userData)
                .patch(UPDATE_ACCOUNT);
    }


    @Step("Удаление аккаунта")
    @Description("Передаем токен и вызываем метод Delete")
    public Response executeAccountRemoval(String accessToken) {
        setUrl();
        return given()
                .header("Authorization", accessToken)
                .delete(REMOVE_ACCOUNT);
    }
}




