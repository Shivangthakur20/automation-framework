package tests;

import base.BaseTest;
import core.config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameworkHealthTest extends BaseTest {

    @Test(groups = {"framework", "smoke"})
    public void config_shouldExposeBaseUrl() {

        String baseUrl = ConfigReader.get("base.url");

        Assert.assertNotNull(baseUrl, "base.url should not be null");
        Assert.assertFalse(baseUrl.isEmpty(), "base.url should not be empty");
    }

    @Test(groups = {"framework", "smoke"})
    public void ui_canOpenBaseUrl() {

        String baseUrl = ConfigReader.get("base.url");

        getDriver().get(baseUrl);

        String title = getDriver().getTitle();

        Assert.assertNotNull(title, "Page title should not be null");
    }
}

