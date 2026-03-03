package api.tests;

import api.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertEquals;

public class HealthCheckTest extends ApiBaseTest {

    @Test(groups = {"api", "smoke"})
    public void healthCheck_shouldRespondQuickly() {

        Response response = request
                .when()
                .get("/")
                .then()
                .time(lessThan(2_000L))
                .extract()
                .response();

        assertEquals(response.getStatusCode(), 200,
                "Expected 200 OK from base URL");
    }
}

