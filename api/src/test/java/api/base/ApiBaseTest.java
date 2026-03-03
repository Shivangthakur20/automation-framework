package api.base;

import core.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class ApiBaseTest {

    protected RequestSpecification request;

    @BeforeClass(alwaysRun = true)
    public void setupApi() {

        String baseUrl = ConfigReader.get("base.url");

        RestAssured.baseURI = baseUrl;

        this.request = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("Accept", "application/json");
    }
}

