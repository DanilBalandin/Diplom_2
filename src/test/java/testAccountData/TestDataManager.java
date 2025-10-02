package testAccountData;

import accountProfile.Account;
import accountProfile.AccountSteps;

import static org.apache.http.HttpStatus.SC_OK;

//Класс для управления
public class TestDataManager {

    private static final AccountSteps accountSteps = new AccountSteps();


    public static String registerAccountAndGetToken(Account account) {
        accountSteps.createNewUser(account);
        return accountSteps.Authentication(account)
                .then()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
    }

    public static void safelyDeleteAccount(Account account) {
        if (isAccountDeletable(account)) {
            try {
                String accessToken = accountSteps.Authentication(account)
                        .then()
                        .extract()
                        .path("accessToken");

                if (accessToken != null) {
                    accountSteps.executeAccountRemoval(accessToken);
                }
            } catch (Exception e) {
                System.out.println("Не удалось удалить аккаунт: " + e.getMessage());
            }
        }
    }

    private static boolean isAccountDeletable(Account account) {
        return account != null &&
                account.getEmail() != null &&
                !account.getEmail().isEmpty() &&
                account.getPassword() != null &&
                !account.getPassword().isEmpty();
    }



}
