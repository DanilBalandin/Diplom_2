package constants;



// Создаю отдельный класс для эндпоинтов, чтобы кучу раз не переписывать одно и тоже, так выглядит красивее
// Тут тоже отдельно выделю для Аккаунта и для Заказа
public class Endpoints {


    public static final class AccountEndpoint {
        public final static String REGISTER_ACCOUNT = "/api/auth/register";
        public final static String AUTHENTICATE_ACCOUNT = "/api/auth/login";
        public final static String UPDATE_ACCOUNT = "/api/auth/user";
        public final static String TERMINATE_SESSION = "/api/v1/logout";
        public final static String REMOVE_ACCOUNT = "/api/auth/user";
    }

    public static final class OrderEndpoint {
        public final static String GET_INGREDIENTS = "/api/ingredients";
        public final static String CREATE_ORDER = "/api/orders";
        public final static String GET_ORDERS = "/api/orders";
    }


}
