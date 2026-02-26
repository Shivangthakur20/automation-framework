package base;

import core.driver.DriverFactory;
import core.metrics.MetricsCollector;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
@Listeners(listeners.TestListener.class)

public class BaseTest {

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeSuite
    public void beforeSuite() {
        MetricsCollector.startSuite();
    }

    @BeforeMethod
    public void setUp() {
        driver.set(DriverFactory.createDriver());
    }

    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driver.remove();
        }
    }

    @AfterSuite
    public void afterSuite() {
        MetricsCollector.endSuite();
    }
}