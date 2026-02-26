package tests;

import base.BaseTest;
import core.config.ConfigReader;
import io.qameta.allure.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;


@Epic("Search Feature")
@Feature("Google Search")
public class AllureTest extends BaseTest {
    private static final Logger log =
            LogManager.getLogger(AllureTest.class);
    @Test(groups = {"smoke"})
    @Story("User searches keyword")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Google search works")
    public void smokeTest_googleSearch() {
        getDriver().get(ConfigReader.get("base.url"));
        // intentionally fail for demo

        log.info("Starting Login Test");

        log.warn("This is warning example");

        log.error("This is error example");
        throw new RuntimeException("Intentional failure");
    }
}


