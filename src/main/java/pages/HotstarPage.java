package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HotstarPage extends BasePage {

    private final By loginButton = By.xpath("//span[text()='Login']");
    private final By sportsTab = By.xpath("//a[contains(text(),'Sports')]");
    private final By liveTab = By.xpath("//a[contains(text(),'Live')]");
    private final By videoPlayer = By.tagName("video");

    public HotstarPage(WebDriver driver) {
        super(driver);
    }

    public void clickLogin() {
        actions.click(loginButton);
    }

    public void openSports() {
        actions.click(sportsTab);
    }

    public void openLive() {
        actions.click(liveTab);
    }

    public boolean isVideoVisible() {
        return actions.isElementDisplayed(videoPlayer);
    }
}