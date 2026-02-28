package core.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Logger log =
            LogManager.getLogger(ScreenshotUtil.class);

    private ScreenshotUtil() {}

    public static void capture(WebDriver driver, String testName) {

        if (driver == null) {
            log.warn("Screenshot skipped. WebDriver is null for test: {}", testName);
            return;
        }

        try {

            log.info("Capturing screenshot for test: {}", testName);

            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            // Attach to Allure
            Allure.getLifecycle().addAttachment(
                    testName,
                    "image/png",
                    "png",
                    screenshot
            );

            saveToDisk(testName, screenshot);

            log.info("Screenshot captured successfully for test: {}", testName);

        } catch (Exception e) {

            log.error("Failed to capture screenshot for test: {}", testName, e);
        }
    }

    private static void saveToDisk(String testName, byte[] data)
            throws Exception {

        String timestamp =
                LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("yyyyMMdd_HHmmss"));

        Path path = Path.of(
                "target/screenshots",
                testName + "_" + timestamp + ".png"
        );

        Files.createDirectories(path.getParent());

        try (FileOutputStream fos =
                     new FileOutputStream(path.toFile())) {

            fos.write(data);
        }

        log.debug("Screenshot saved to disk at: {}", path.toAbsolutePath());
    }
}