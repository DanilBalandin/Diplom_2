package tests;

import accountProfile.Account;
import accountProfile.AccountSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;
import testAccountData.TestDataManager;

import java.util.ArrayList;
import java.util.List;

import static constants.Response.AccountResponses.INVALID_AUTHENTICATION_CREDENTIALS;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestLoginAccount {
    private final AccountSteps accountSteps = new AccountSteps();
    public Account account;
    public String accessToken;
    private final List<Account> createdAccounts = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        account = TestAccountBuilder.createRandomAccount();
        accessToken = TestDataManager.registerAccountAndGetToken(account);
    }

    @Test
    @DisplayName("Успешная аутентификация")
    public void testLogin() {
        Account testAccount = TestAccountBuilder.createRandomAccount();
        createdAccounts.add(testAccount);

        accountSteps.createNewUser(testAccount);
        accountSteps.authenticate(testAccount).then()
                .statusCode(SC_OK)
                .extract()
                .path("success", String.valueOf(equalTo("true")));

    }

    @Test
    @DisplayName("Аутентификация с неправильным email")
    public void testLoginWrongEmail() {
        Account correctAccount = TestAccountBuilder.createRandomAccount();
        accountSteps.createNewUser(correctAccount);
        Account incorrectEmailAccount = TestAccountBuilder.createAccountWithWrongEmail(correctAccount);
        createdAccounts.add(correctAccount);

        accountSteps.authenticate(incorrectEmailAccount)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(INVALID_AUTHENTICATION_CREDENTIALS));
    }

    @Test
    @DisplayName("Аутентификация с неправильным password")
    public void testLoginWrongPassword() {
        Account correctAccount = TestAccountBuilder.createRandomAccount();
        accountSteps.createNewUser(correctAccount);
        Account incorrectPasswordAccount = TestAccountBuilder.createAccountWithWrongPassword(correctAccount);
        createdAccounts.add(correctAccount);


        accountSteps.authenticate(incorrectPasswordAccount)
                .then()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(INVALID_AUTHENTICATION_CREDENTIALS));
    }
        @AfterEach
        public void tearDown () {
            for (Account account : createdAccounts) {
                TestDataManager.safelyDeleteAccount(account);
            }
            createdAccounts.clear();
        }

}

