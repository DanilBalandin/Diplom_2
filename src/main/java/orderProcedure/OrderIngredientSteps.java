package orderProcedure;

import constants.Url;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static constants.Endpoints.OrderEndpoint.GET_INGREDIENTS;
import static io.restassured.RestAssured.given;



public class OrderIngredientSteps extends Url {


   private final Order order;

    public OrderIngredientSteps(Order order) {
        this.order = order;
    }


    @Step("Список доступных ингредиентов")
    @Description("Будем получать список доступных ингредиентов")
    public Response requestIngredientsList() {
        setUrl();
        return given()
                .get(GET_INGREDIENTS);
    }


    public List<String> getIngredientsList() {
        List<String> ingredients = requestIngredientsList()
                .then()
                .extract()
                .path("data._id");
        List<String> selectedIngredients  = order.getIngredients();
        for (int i = 0; i <= 8; i += 2) {
            selectedIngredients.add(ingredients.get(i));

        }
        return selectedIngredients;
    }
}
