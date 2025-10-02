package tests;

import accountProfile.Account;
import io.restassured.response.Response;
import orderProcedure.Order;
import orderProcedure.OrderIngredientSteps;
import orderProcedure.OrderSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;
import testAccountData.TestDataManager;

import java.util.ArrayList;
import java.util.List;

import static constants.Response.OrderResponses.MISSING_INGREDIENTS_VALIDATION;
import static org.apache.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestCreateOrder {

    private final OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private OrderIngredientSteps orderIngredientSteps;
    public Account account;
    private String accessToken;
    private final List<Account> createdAccounts = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        account = TestAccountBuilder.createRandomAccount();
        accessToken = TestDataManager.registerAccountAndGetToken(account);
        order = new Order();
        orderIngredientSteps = new OrderIngredientSteps(order);
        createdAccounts.add(account);

    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией")
    public void createOrderWithLogin() {
        orderIngredientSteps.getIngredientsList();
        Response response = orderSteps.createOrder(accessToken, order);

        boolean orderResponse = response
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("success");


        assertTrue(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")

    public void createOrderWithoutLogin() {
        orderIngredientSteps.getIngredientsList();
        Response response = orderSteps.createOrder("", order);

        boolean orderResponse = response
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("success");

        assertTrue(orderResponse);

    }

    @Test
    @DisplayName("Проверка ошибки 400")
    public void orderWithoutComponents() {
        Response response = orderSteps.createOrder(accessToken, order);

        boolean orderResponse = response
                .then()
                .statusCode(SC_BAD_REQUEST)
                .extract()
                .path("success");

        String message = response
                .then()
                .extract()
                .path("message");

        assertFalse(orderResponse);
        assertEquals(MISSING_INGREDIENTS_VALIDATION, message);
    }

    @Test
    @DisplayName("Неверные данные")
    public void orderWithInvalidComponents() {
        List<String> validIngredients = orderIngredientSteps.getIngredientsList();
        List<String> invalidIngredients = new ArrayList<>();

        if (!validIngredients.isEmpty()) {

            String firstInvalid = validIngredients.get(0).replaceAll("\\d", "X");
            invalidIngredients.add(firstInvalid);


            if (validIngredients.size() > 1) {
                String secondInvalid = validIngredients.get(1).replaceAll("f", "X");
                invalidIngredients.add(secondInvalid);
            }
        }

        order.setIngredients(invalidIngredients);


        orderSteps.createOrder(accessToken, order)
                .then()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @AfterEach
    public void tearDown () {
        for (Account account : createdAccounts) {
            TestDataManager.safelyDeleteAccount(account);
        }
        createdAccounts.clear();
    }


}
