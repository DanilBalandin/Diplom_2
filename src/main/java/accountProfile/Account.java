package accountProfile;


//Тут ничего необычного, просто аккаунт юзера, конструкторы, его геттеры и сеттеры
// Вообще я ознакамливался с лумбуком, чтобы его тут использоваться (на работе просто его юзают), но не стал.
public class Account {

    private String email;
    private String name;
    private String password;

    public Account(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }



    // Пригодится для шага с завершением сеанса
    public static class SessionTerminationPayload {
        private final String refreshToken;

        public SessionTerminationPayload(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

}
