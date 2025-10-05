package tests;

import accountProfile.Account;
import accountProfile.AccountSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;



import static constants.Response.AccountResponses.INVALID_AUTHENTICATION_CREDENTIALS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestLoginAccount {
    private final AccountSteps accountSteps = new AccountSteps();
    public Account account;
    public String accessToken;


    @BeforeEach
    public void setUp() {
        account = TestAccountBuilder.createRandomAccount();
        Response registerAccount = accountSteps.createNewUser(account);
        accessToken = registerAccount.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Успешная аутентификация")
    public void testLogin() {

        accountSteps.authenticate(account).then()
                .statusCode(SC_OK)
                .extract()
                .path("success", String.valueOf(equalTo("true")));


    }

    @Test
    @DisplayName("Аутентификация с неправильным email")
    public void testLoginWrongEmail() {

        Account incorrectEmailAccount = TestAccountBuilder.createAccountWithWrongEmail(account);


        accountSteps.authenticate(incorrectEmailAccount)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(INVALID_AUTHENTICATION_CREDENTIALS));
    }

    @Test
    @DisplayName("Аутентификация с неправильным password")
    public void testLoginWrongPassword() {

        Account incorrectPasswordAccount = TestAccountBuilder.createAccountWithWrongPassword(account);


        accountSteps.authenticate(incorrectPasswordAccount)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(INVALID_AUTHENTICATION_CREDENTIALS));
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
