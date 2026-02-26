package pages;

import org.openqa.selenium.WebDriver;
import utils.ElementActions;

public class BasePage {

    protected WebDriver driver;
    protected ElementActions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new ElementActions(driver);
    }


}