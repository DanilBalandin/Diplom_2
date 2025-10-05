package tests;

import accountProfile.Account;
import accountProfile.AccountSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;

import static constants.Response.AccountResponses.DUPLICATE_ACCOUNT;
import static constants.Response.AccountResponses.INCOMPLETE_REGISTRATION_DATA;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;


public class TestCreateAccount {

    private final AccountSteps accountSteps = new AccountSteps();
    public Account account;
    public String accessToken;


    @BeforeEach
    public void setUp() {
        account = TestAccountBuilder.createRandomAccount();
    }

    @Test
    @DisplayName("Регистрация нового рандомного аккаунта")
    public void createNewAccount() {

        accountSteps.createNewUser(account)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        Response loginResponse = accountSteps.authenticate(account);
        accessToken = loginResponse.jsonPath().getString("accessToken");


    }

    @Test
    @DisplayName("Регистрация на тот же аккаунт")
    public void createDuplicateAccount() {

        accountSteps.createNewUser(account).then()
                .assertThat()
                .statusCode(SC_OK);

        Response loginResponse = accountSteps.authenticate(account);
        accessToken = loginResponse.jsonPath().getString("accessToken");

        accountSteps.createNewUser(account).then()
                .assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(DUPLICATE_ACCOUNT));
    }

    @Test
    @DisplayName("Регистрация без email")
    public void createAccountWithoutEmail() {

        account = TestAccountBuilder.createAccountWithoutEmail();
        accountSteps.createNewUser(account).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }

    @Test
    @DisplayName("Регистрация без password")
    public void createAccountWithoutPassword() {

        account = TestAccountBuilder.createAccountWithoutPassword();
        accountSteps.createNewUser(account).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }

    @Test
    @DisplayName("Регистрация без name")
    public void createAccountWithoutName() {

        account = TestAccountBuilder.createAccountWithoutName();
        accountSteps.createNewUser(account).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }


    @AfterEach
    public void tearDown() {

        if (accessToken != null) {
            try {
                accountSteps.deleteAccount(accessToken);
            } catch (Exception e) {
                System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            }
        }


    }
}

