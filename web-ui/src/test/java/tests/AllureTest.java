package tests;

import base.BaseTest;
import core.config.ConfigReader;
import io.qameta.allure.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.openqa.selenium.support.locators.RelativeLocator.with;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

        log.info("Starting Login Test");
        log.warn("This is warning example");
        log.error("This is error example");

        WebElement passwordField = getDriver().findElement(By.id("input-password"));
// Locate the email field above the password field
        WebElement emailField = getDriver().findElement(with(By.tagName("input")).above(passwordField));
        emailField.sendKeys("test@email.com");
        String title = getDriver().getTitle();
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");
    }
}

