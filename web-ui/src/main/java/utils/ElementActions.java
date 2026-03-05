package utils;

import constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.JavascriptExecutor;

public class ElementActions {

    private static final Logger log =
            LogManager.getLogger(ElementActions.class);

    private final WebDriver driver;
    private final WaitUtils wait;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    /* ================= CLICK ================= */

    public void click(By locator) {
        try {
            log.info("Clicking on element: {}", locator);
            wait.waitForClickable(locator,
                    FrameworkConstants.DEFAULT_WAIT).click();
        } catch (Exception e) {
            log.warn("Normal click failed. Trying JS click for: {}", locator);
            jsClick(locator);
        }
    }

    /* ================= TYPE ================= */

    public void type(By locator, String value) {
        log.info("Typing '{}' into element: {}", value, locator);

        WebElement element =
                wait.waitForVisibility(locator,
                        FrameworkConstants.DEFAULT_WAIT);

        element.clear();
        element.sendKeys(value);
    }

    /* ================= PRESS ENTER ================= */

    public void pressEnter(By locator) {
        log.info("Pressing ENTER on: {}", locator);

        wait.waitForVisibility(locator,
                        FrameworkConstants.DEFAULT_WAIT)
                .sendKeys(Keys.ENTER);
    }

    /* ================= GET TEXT ================= */

    public String getText(By locator) {
        return wait.waitForVisibility(locator,
                        FrameworkConstants.DEFAULT_WAIT)
                .getText();
    }

    /* ================= DISPLAY CHECK ================= */

    public boolean isElementDisplayed(By locator) {
        try {
            wait.waitForVisibility(locator,
                    FrameworkConstants.DEFAULT_WAIT);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /* ================= PRESENCE CHECK ================= */

    public boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /* ================= JS CLICK FALLBACK ================= */

    private void jsClick(By locator) {
        WebElement element =
                wait.waitForVisibility(locator,
                        FrameworkConstants.DEFAULT_WAIT);

        JavascriptExecutor js =
                (JavascriptExecutor) driver;

        js.executeScript("arguments[0].click();", element);
    }
}

