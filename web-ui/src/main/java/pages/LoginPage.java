package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private By username = By.id("email");
    private By password = By.id("password");
    private By loginBtn = By.id("login");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String user, String pass) {
        actions.type(username, user);
        actions.type(password, pass);
        actions.click(loginBtn);
    }

    public boolean isLoginSuccessful() {
        return driver.getTitle().contains("Home");
    }
}

