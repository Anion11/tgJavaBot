import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
public class TestCases {
    private static final String BASE_URL = "https://api.telegram.org/bot7189595273:AAEL_fOhdd9xY31YnFwYakR0mTNAfT6PQ5E";
    @Test
    public void testTelegramBotUserRequest() {
        given()
                .contentType("application/json")
                .body("{ \"chat_id\": \"721116972\", \"text\": \"/start!\" }")
                .when()
                .post(BASE_URL + "/sendMessage")
                .then()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}