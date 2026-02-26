package core.driver;

import core.config.ConfigReader;
import core.config.FeatureToggle;
import core.grid.GridManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class DriverFactory {

    public static WebDriver createDriver() {

        String browser = ConfigReader.get("browser");

        if (FeatureToggle.GRID.isEnabled()) {
            return createRemote(browser);
        }

        return createLocal(browser);
    }

    private static WebDriver createLocal(String browser) {

        switch (browser.toLowerCase()) {

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();

            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                return new ChromeDriver(options);
        }
    }

    private static WebDriver createRemote(String browser) {

        try {
            URL gridUrl = GridManager.getGridUrl();

            switch (browser.toLowerCase()) {
                case "firefox":
                    return new RemoteWebDriver(gridUrl,
                            new FirefoxOptions());
                default:
                    return new RemoteWebDriver(gridUrl,
                            new ChromeOptions());
            }

        } catch (Exception e) {
            throw new RuntimeException("Grid creation failed", e);
        }
    }
}