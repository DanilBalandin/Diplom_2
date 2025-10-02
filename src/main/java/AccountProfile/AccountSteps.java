package AccountProfile;

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
    public Response Authentication(Account userData) {
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
    public Response AccountUpdate(String accessToken, Account userData) {
        setUrl();
        return given()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(userData)
                .patch(UPDATE_ACCOUNT);
    }

    // Завершаем пользовательскую сессию
    @Step("Завершение сеанса")
    @Description("Вытягиваем refreshToken из логина и отправляем Post запрос")
    public Response TermSession(Account userData) {
        setUrl();
        // Зашиваю в refreshToken сам токен, вытягивая его из ответа при логине
        String refreshToken = Authentication(userData).then().extract().path("refreshToken");
        return given()
                .header("Content-type", "application/json")
                .body(new Account.SessionTerminationPayload(refreshToken))
                .post(TERMINATE_SESSION);
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




