package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GooglePage {
    WebDriver driver;

    @FindBy(name = "q")
    WebElement searchBox;

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void searchFor(String text) {
        searchBox.sendKeys(text);
        searchBox.submit();
    }
}

