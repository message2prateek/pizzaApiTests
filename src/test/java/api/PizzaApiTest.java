package api;

import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PizzaApiTest {

    @Test
    // Validating status code, header and body
    public void getSinglePizzaTest() {

        given()
                // .header("Accept", "application/json")
                .accept(ContentType.JSON)
        .when()
                .get("http://localhost:3000/pizzaOrders/1")
        .then()
                .statusCode(200)
                .header("content-type", "application/json;charset=UTF-8")
                .body("pizzaName", equalTo("Veggie Lovers"))
                .body("crust", equalTo("thin"))
                .body("toppings[0]", equalTo("Mushroom"));

    }


    //Handling JSON Arrays
    @Test
    public void getAllPizzasTest() {

        final List<String> tags = Arrays.asList("eatable", "savoury");

        given()
            .header("Accept", "application/json")
        .when()
            .get("http://localhost:3000/pizzaOrders")
        .then()
            .assertThat()
                .statusCode(200)
                // number of items present in an array
                .body("pizzaOrders", iterableWithSize(2))
                .body("pizzaOrders[0].toppings", iterableWithSize(1))
                .body("pizzaOrders[0].sides", iterableWithSize(2))
                // is an item present in an Array
                .body("pizzaOrders[0].toppings", hasItem("Mushroom"))
                // are multiple items present in an Array, order does not matter
                .body("pizzaOrders[0].sides[0].tags", hasItems("savoury", "eatable"))
                // To check the entire array, including the ordering.
                .body("pizzaOrders[0].sides[0].tags", equalTo(tags));
    }

}
