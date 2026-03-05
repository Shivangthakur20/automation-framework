package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void verifyUserLogin() {

        getDriver().get("https://example.com/login");

        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("abc", "xyz");

        Assert.assertTrue(
                loginPage.isLoginSuccessful(),
                "Login failed"
        );
    }
}

