package core.driver;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private DriverManager() {}

    private static final ThreadLocal<WebDriver> driverThreadLocal =
            new ThreadLocal<>();

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {

        WebDriver driver = driverThreadLocal.get();

        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    public static void unload() {
        driverThreadLocal.remove();
    }
}