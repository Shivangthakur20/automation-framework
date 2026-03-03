package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class HotstarTest extends BaseTest {

    @Test(groups = {"Hotstar"})
    public void hotstarLoginTest() {
        getDriver().get("http://google.com");
        getDriver().findElement(By.xpath("//textarea[@name='q']")).sendKeys("Hotstar");
    }
}

