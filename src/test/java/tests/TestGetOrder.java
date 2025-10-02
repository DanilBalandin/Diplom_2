package tests;

import accountProfile.Account;
import io.qameta.allure.Description;
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

import static constants.Response.AccountResponses.UNAUTHORIZED_OPERATION;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestGetOrder {

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
    @DisplayName("Смотрим историю авторизованного п-ля")
    @Description("Verify successful retrieval of transaction history for authenticated account holder")
    public void getOrderAuthorizedTest() {
        List<String> ingredients = orderIngredientSteps.getIngredientsList();
        order.setIngredients(ingredients);

        orderSteps.createOrder(accessToken, order);

        Response getOrdersResponse = orderSteps.getOrderHistoryUser(accessToken);
        getOrdersResponse.then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Смотрим историю для неавторизованного")
    public void getOrderUnauthorizedTest() {

        Response getOrdersResponse = orderSteps.getOrderHistoryUser("");
        getOrdersResponse.then()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(UNAUTHORIZED_OPERATION));

    }

    @AfterEach
    public void tearDown () {
        for (Account account : createdAccounts) {
            TestDataManager.safelyDeleteAccount(account);
        }
        createdAccounts.clear();
    }
}