package base;

import core.config.ConfigReader;
import core.driver.DriverFactory;
import core.metrics.MetricsCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;

@Listeners(listeners.TestListener.class)
public class BaseTest {

    private static final Logger log =
            LogManager.getLogger(BaseTest.class);

    private static final ThreadLocal<WebDriver> driver =
            new ThreadLocal<>();

    public WebDriver getDriver() {
        return driver.get();
    }

    /* ================= SUITE START ================= */

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {

        String suiteName =
                context.getSuite().getName();

        log.info("===== STARTING SUITE: {} =====", suiteName);

        MetricsCollector.startSuite();
    }

    /* ================= TEST SETUP ================= */

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        log.info("Creating WebDriver instance");

        driver.set(DriverFactory.createDriver());
    }

    /* ================= TEST CLEANUP ================= */

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (getDriver() != null) {

            log.info("Closing WebDriver instance");

            getDriver().quit();
            driver.remove();
        }
    }

    /* ================= SUITE END ================= */

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {

        String suiteName =
                context.getSuite().getName();

        String environment =
                ConfigReader.get("env");

        MetricsCollector.endSuite(
                suiteName,
                environment
        );

        log.info("===== SUITE FINISHED: {} | ENV: {} =====",
                suiteName,
                environment);
    }
}