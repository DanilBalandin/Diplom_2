package orderProcedure;

import java.util.ArrayList;
import java.util.List;


//Класс для заказа, геттер для списка ингредиентов. Возможно понадобятся еще поля класса и их геттеры/сеттеры
public class Order {

    private List<String> ingredients;

    public Order() {
        ingredients = new ArrayList<>();
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }


}

