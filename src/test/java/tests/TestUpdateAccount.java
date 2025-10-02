package tests;

import accountProfile.Account;
import accountProfile.AccountSteps;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testAccountData.TestAccountBuilder;
import testAccountData.TestDataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static constants.Response.AccountResponses.UNAUTHORIZED_OPERATION;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

//Сломал себе всю голову как это правильно использовать вместе с билдером и менеджером, потом понял, что очень
// хочу усложнить логику, но разве мы не должны ее наоборот упрощать? Поэтому тут я использовал обычный подход
public class TestUpdateAccount {
    private final AccountSteps accountSteps = new AccountSteps();
    private Account account;
    private String accessToken;
    private List<Account> createdAccounts = new ArrayList<>();



    @BeforeEach
    public void setUp() {
        account = TestAccountBuilder.createRandomAccount();
        accessToken = TestDataManager.registerAccountAndGetToken(account);
    }

    @Test
    @DisplayName("Обновляем мыло")
    public void emailAddressUpdate() {
        String newEmail = "user." + UUID.randomUUID().toString().substring(0, 8) + "@yandex.ruhaha";
        Account updatedAccountData = new Account(newEmail, account.getName(), account.getPassword());

        String actualEmail = accountSteps.AccountUpdate(accessToken,updatedAccountData ).then()
                .assertThat()
                .statusCode(SC_OK)  // Добавляем проверку статуса
                .extract()
                .path("user.email");


        account = updatedAccountData;
        createdAccounts.add(account);
        assertEquals(newEmail, actualEmail);
    }

    @Test
    @DisplayName("Обновляем имя")
    public void accountNameUpdate() {
        String newName = "TestAccount_" + UUID.randomUUID().toString().substring(0, 8);
        Account updatedAccountData = new Account(account.getEmail(), newName, account.getPassword());

        String actualName = accountSteps.AccountUpdate(accessToken, updatedAccountData).then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("user.name");

        account = updatedAccountData;
        createdAccounts.add(account);
        assertEquals(newName, actualName);
    }

    @Test
    @DisplayName("Обновляем пароль")
    public void passwordUpdate() {
        String newPassword = "samplePassword@" + UUID.randomUUID().toString().substring(0, 8);
        Account updatedAccountData = new Account(account.getEmail(), account.getName(), newPassword);

        Boolean success = accountSteps.AccountUpdate(accessToken, updatedAccountData).then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");

        account = updatedAccountData;
        createdAccounts.add(account);
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Проверка ошибки, без авторизации")
    public void updateWithoutAuthorization() {
        Account clientNewEmail = new Account(account.getEmail(), account.getName(), account.getPassword());
        accountSteps.AccountUpdate("", clientNewEmail)
                .then()
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
