package testAccountData;

import accountProfile.Account;

import java.util.UUID;


//Планирую вынести создание тестовых-аккаунтов в отдельный "менеджер аккаунтов"

public class TestAccountBuilder {

    //Создаем рандомный аккаунт
    public static Account createRandomAccount() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new Account(
                "user." + uniqueId + "@testmail.com",
                "TestAccount_" + uniqueId,
                "samplePassword@" + uniqueId
        );
    }

    public static Account createAccountWithSpecificEmail(String email) {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new Account(
                email,
                "TestAccount_" + uniqueId,
                "samplePassword@" + uniqueId
        );

    }
    //Без почты
    public static Account createAccountWithoutEmail() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new Account(
                null,
                "TestAccount_" + uniqueId,
                "samplePassword@" + uniqueId
        );
    }
    //Без пароля
    public static Account createAccountWithoutPassword() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new Account(
                "user." + uniqueId + "@testmail.com",
                "TestAccount_" + uniqueId,
                null
        );
    }

    //Без имени
    public static Account createAccountWithoutName() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return new Account(
                "user." + uniqueId + "@testmail.com",
                null,
                "samplePassword@" + uniqueId
        );
    }

}