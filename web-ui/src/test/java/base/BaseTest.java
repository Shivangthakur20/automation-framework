package base;

import core.driver.DriverFactory;
import core.driver.DriverManager;
import core.reporting.AllureAttachmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import org.openqa.selenium.WebDriver;

public abstract class BaseTest {

    private static final Logger log =
            LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        log.info("Creating WebDriver instance");

        WebDriver driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
    }

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            try {
                if (result.getStatus() == ITestResult.FAILURE) {
                    AllureAttachmentService.attachScreenshot(driver);
                    AllureAttachmentService.attachPageSource(driver);
                    AllureAttachmentService.attachConsoleLogs(driver);
                }
            } catch (Exception e) {
                log.debug("Attachment failed: {}", e.getMessage());
            }
            try {
                driver.quit();
            } catch (Exception e) {
                log.debug("Driver quit failed (session may be invalid): {}", e.getMessage());
            } finally {
                DriverManager.unload();
            }
        }
    }
}

