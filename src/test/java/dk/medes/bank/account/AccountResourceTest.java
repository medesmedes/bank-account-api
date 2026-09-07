package dk.medes.bank.account;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;

@QuarkusTest
class AccountResourceTest {

    @Test
    void createReturnsAccountNumber() {
        given()
                .contentType("application/json")
                .body("{\"firstName\":\"Ada\",\"lastName\":\"Lovelace\"}")
                .when().post("/accounts")
                .then()
                .statusCode(201)
                .body("accountNumber", startsWith("DKBD"))
                .body("balance", is(0.0f));
    }

    @Test
    void depositIncreasesBalance() {
        String account = createAccount();

        given()
                .contentType("application/json")
                .body("{\"amount\":100.00}")
                .when().post("/accounts/" + account + "/deposit")
                .then()
                .statusCode(200)
                .body("balance", is(100.0f));
    }

    @Test
    void transferMovesFunds() {
        String from = createAccount();
        String to = createAccount();
        given().contentType("application/json").body("{\"amount\":100.00}")
                .when().post("/accounts/" + from + "/deposit").then().statusCode(200);

        given()
                .contentType("application/json")
                .body("{\"toAccountNumber\":\"" + to + "\",\"amount\":40.00}")
                .when().post("/accounts/" + from + "/transfer")
                .then()
                .statusCode(200)
                .body("balance", is(60.0f));
    }

    private String createAccount() {
        return given()
                .contentType("application/json")
                .body("{\"firstName\":\"Test\",\"lastName\":\"User\"}")
                .when().post("/accounts")
                .then().statusCode(201)
                .extract().path("accountNumber");
    }
}
