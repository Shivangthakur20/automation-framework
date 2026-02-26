package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriverWait getWait(int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds));
    }

    public WebElement waitForVisibility(By locator, int seconds) {
        return getWait(seconds)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator, int seconds) {
        return getWait(seconds)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean waitForTitleContains(String title, int seconds) {
        return getWait(seconds)
                .until(ExpectedConditions.titleContains(title));
    }
}