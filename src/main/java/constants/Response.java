package constants;



/* Здесь находятся эндпоинты, чтобы обращаться к ним и было меньше кода.
Чтобы было красиво, хочу отдельно выделить респонсы для Аккаунта и для заказов
 */

public class Response {

// Тут буду хранить респонсы для аккаунта
    public static final class AccountResponses {
    public static final String DUPLICATE_ACCOUNT =
            "{\"success\":false,\"message\":\"User already exists\"}";
    public static final String INCOMPLETE_REGISTRATION_DATA =
            "{\"success\":false,\"message\":\"Email, password and name are required fields\"}";
    public static final String INVALID_AUTHENTICATION_CREDENTIALS =
            "{\"success\":false,\"message\":\"email or password are incorrect\"}";
    public static final String UNAUTHORIZED_OPERATION =
            "{\"success\":false,\"message\":\"You should be authorised\"}";
}
    // А тут хранится респонс для заказа
    public static final class OrderResponses {
        public static final String MISSING_INGREDIENTS_VALIDATION =
                "Ingredient ids must be provided";

    }
}
