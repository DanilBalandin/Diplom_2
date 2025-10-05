package orderProcedure;

import constants.Url;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static constants.Endpoints.OrderEndpoint.CREATE_ORDER;
import static constants.Endpoints.OrderEndpoint.GET_ORDERS;
import static io.restassured.RestAssured.given;

public class OrderSteps extends Url {

    @Step("Создание заказа")
    @Description("Передаем токен и данные заказа, вызываем метод Post по эндпоинту, чтобы его создать")
    public Response createOrder(String accessToken, Order order) {
        setUrl();
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(CREATE_ORDER);
    }

    @Step("Заказы пользователя")
    @Description("Будем получать список заказов пользователя")
    public Response getOrderHistoryUser(String accessToken) {
        setUrl();
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(GET_ORDERS);
    }
}
