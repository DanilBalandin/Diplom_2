package constants;


import io.restassured.RestAssured;

// Ну я не хотел использовать везде ссылку, это был бы копипаст постоянный, поэтому хочу сюда ее вставить.
public abstract class Url {
    private final static String URL = "https://stellarburgers.nomoreparties.site";


    // тут протектед, ибо только наследникам даем такое право
    protected void setUrl() {
        RestAssured.baseURI = URL;
    }
}

