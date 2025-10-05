package tests;

import accountProfile.Account;
import accountProfile.AccountSteps;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;

import java.util.UUID;

import static constants.Response.AccountResponses.UNAUTHORIZED_OPERATION;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestUpdateAccount {
    private final AccountSteps accountSteps = new AccountSteps();
    private Account account;
    private String accessToken;



    @BeforeEach
    public void setUp() {

        account = TestAccountBuilder.createRandomAccount();
        Response registerAccount = accountSteps.createNewUser(account);
        accessToken = registerAccount.jsonPath().getString("accessToken");

    }

    @Test
    @DisplayName("Обновляем мыло")
    public void emailAddressUpdate() {
        String newEmail = "user." + UUID.randomUUID().toString().substring(0, 8) + "@yandex.ruhaha";
        Account updatedAccountData = new Account(newEmail, account.getName(), account.getPassword());

        String actualEmail = accountSteps.accountUpdate(accessToken,updatedAccountData ).then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("user.email");


        account = updatedAccountData;
        assertEquals(newEmail, actualEmail);
    }

    @Test
    @DisplayName("Обновляем имя")
    public void accountNameUpdate() {
        String newName = "TestAccount_" + UUID.randomUUID().toString().substring(0, 8);
        Account updatedAccountData = new Account(account.getEmail(), newName, account.getPassword());

        String actualName = accountSteps.accountUpdate(accessToken, updatedAccountData).then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("user.name");

        account = updatedAccountData;
        assertEquals(newName, actualName);
    }

    @Test
    @DisplayName("Обновляем пароль")
    public void passwordUpdate() {
        String newPassword = "samplePassword@" + UUID.randomUUID().toString().substring(0, 8);
        Account updatedAccountData = new Account(account.getEmail(), account.getName(), newPassword);

        Boolean success = accountSteps.accountUpdate(accessToken, updatedAccountData).then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");

        account = updatedAccountData;
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки, без авторизации")
    public void updateWithoutAuthorization() {
        Account clientNewEmail = new Account(account.getEmail(), account.getName(), account.getPassword());
        accountSteps.accountUpdate("", clientNewEmail)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body(equalTo(UNAUTHORIZED_OPERATION));
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
