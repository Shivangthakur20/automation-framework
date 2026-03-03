package e2e;

import core.config.ConfigReader;
import core.driver.DriverFactory;
import core.driver.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Skeleton E2E test showing how to wire UI + core in a dedicated module.
 * Replace the URL and assertions with a real business flow when you integrate
 * with your application under test.
 */
public class BasicE2ETest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            driver.quit();
            DriverManager.unload();
        }
    }

    @Test(groups = {"e2e"})
    public void sampleE2E_smoke() {
        WebDriver driver = DriverManager.getDriver();
        String baseUrl = ConfigReader.getOrDefault("base.url", "https://www.google.com");
        driver.get(baseUrl);
        String title = driver.getTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }
}

