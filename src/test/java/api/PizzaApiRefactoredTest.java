package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PizzaApiRefactoredTest {

    private static RequestSpecification requestSpec;

    private static ResponseSpecification responseSpec;

    @BeforeClass
    public static void setup() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(3000)
                .addHeader("Accept", "application/json")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json;charset=UTF-8")
                .build();

    }


    @Test
    public void getSinglePizzaTest() {

        given()
                .spec(requestSpec)
        .when()
                .get("/pizzaOrders/1")
        .then()
                .assertThat()
                    .spec(responseSpec)
                    .body("pizzaName", equalTo("Veggie Lovers"))
                    .body("crust", equalTo("thin"))
                    .body("toppings[0]", equalTo("Mushroom"));

    }


    //Handling JSON Arrays
    @Test

    public void getAllPizzasTest() {

        final List<String> tags = Arrays.asList("eatable", "savoury");

        given()
            .spec(requestSpec)
        .when()
            .get("/pizzaOrders")
        .then()
            .assertThat()
                .spec(responseSpec)
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

    @Test
    public void validateCrustForAPizzaOrderWithAGivenName(){

        given()
            .spec(requestSpec)
        .when()
            .get("/pizzaOrders")
        .then()
            .assertThat()
                .rootPath("pizzaOrders.find { pizzaOrder -> pizzaOrder.pizzaName.equals('Veggie Lovers') }")
                .body("crust", equalTo("thin"));
    }


    //Add test to find validate sides for PizzaOrder with Id = 2
    @Test
    public void validatePizzaOrderSides(){

    }


}

