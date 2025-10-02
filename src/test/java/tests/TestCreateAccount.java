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

import static constants.Response.AccountResponses.DUPLICATE_ACCOUNT;
import static constants.Response.AccountResponses.INCOMPLETE_REGISTRATION_DATA;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

//Расскажу логику, так как я вывел менеджера, то удаление данных просто так в AfterEach не засунуть
//Поэтому я делаю список созданных аккаунтов, который потом отчищаю
public class TestCreateAccount {

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
    @DisplayName("Регистрация нового рандомного аккаунта")
    public void createNewAccount() {

        Account testAccount = TestAccountBuilder.createRandomAccount();
        createdAccounts.add(testAccount);

        accountSteps.createNewUser(testAccount)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true));


    }

    @Test
    @DisplayName("Регистрация на тот же аккаунт")
    public void createDuplicateAccount() {

        Account testAccount = TestAccountBuilder.createRandomAccount();


        accountSteps.createNewUser(testAccount).then()
                .assertThat()
                .statusCode(SC_OK);
        accountSteps.createNewUser(testAccount).then()
                .assertThat().statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(DUPLICATE_ACCOUNT));
    }

    @Test
    @DisplayName("Регистрация без email")
    public void createAccountWithoutEmail() {

        Account testAccount = TestAccountBuilder.createAccountWithoutEmail();


        accountSteps.createNewUser(testAccount).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }

    @Test
    @DisplayName("Регистрация без password")
    public void createAccountWithoutPassword() {

        Account testAccount = TestAccountBuilder.createAccountWithoutPassword();


        accountSteps.createNewUser(testAccount).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }

    @Test
    @DisplayName("Регистрация без name")
    public void createAccountWithoutName() {

        Account testAccount = TestAccountBuilder.createAccountWithoutName();

        accountSteps.createNewUser(testAccount).then()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(INCOMPLETE_REGISTRATION_DATA));

    }


    @AfterEach
    public void tearDown() {
        for (Account account : createdAccounts) {
            TestDataManager.safelyDeleteAccount(account);
        }
        createdAccounts.clear();
    }
}

