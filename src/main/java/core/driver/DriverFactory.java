package core.driver;

import core.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {}

    public static WebDriver createDriver() {

        String runMode = System.getProperty(
                "run.mode",
                ConfigReader.get("run.mode")
        ).toLowerCase();

        String browser = System.getProperty(
                "browser",
                ConfigReader.get("browser")
        ).toLowerCase();

        boolean headless = Boolean.parseBoolean(
                System.getProperty(
                        "headless",
                        ConfigReader.get("headless")
                )
        );

        WebDriver driver;

        if ("remote".equals(runMode)) {
            driver = createRemoteDriver(browser, headless);
        } else {
            driver = createLocalDriver(browser, headless);
        }

        // Global stable configuration
        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(0));

        driver.manage().timeouts()
                .pageLoadTimeout(Duration.ofSeconds(60));

        driver.manage().window().maximize();

        return driver;
    }

    /* ================= LOCAL ================= */

    private static WebDriver createLocalDriver(
            String browser,
            boolean headless) {

        switch (browser) {

            case "chrome":

                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions =
                        buildChromeOptions(headless);

                return new ChromeDriver(chromeOptions);

            case "firefox":

                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions firefoxOptions =
                        buildFirefoxOptions(headless);

                return new FirefoxDriver(firefoxOptions);

            default:
                throw new RuntimeException(
                        "Unsupported browser: " + browser
                );
        }
    }

    /* ================= REMOTE ================= */

    private static WebDriver createRemoteDriver(
            String browser,
            boolean headless) {

        try {

            String gridUrl = System.getProperty(
                    "grid.url",
                    ConfigReader.get("grid.url")
            );

            switch (browser) {

                case "chrome":
                    return new RemoteWebDriver(
                            new URL(gridUrl),
                            buildChromeOptions(headless)
                    );

                case "firefox":
                    return new RemoteWebDriver(
                            new URL(gridUrl),
                            buildFirefoxOptions(headless)
                    );

                default:
                    throw new RuntimeException(
                            "Unsupported browser: " + browser
                    );
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create remote driver", e
            );
        }
    }

    /* ================= OPTIONS BUILDERS ================= */

    private static ChromeOptions buildChromeOptions(boolean headless) {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return options;
    }

    private static FirefoxOptions buildFirefoxOptions(boolean headless) {

        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return options;
    }
}