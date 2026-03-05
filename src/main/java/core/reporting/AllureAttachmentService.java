package core.reporting;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public final class AllureAttachmentService {

    private static final Logger log =
            LogManager.getLogger(AllureAttachmentService.class);

    private AllureAttachmentService() {}

    /* ================= SCREENSHOT ================= */


    public static void attachScreenshot(WebDriver driver) {

        try {

            Optional<String> uuid =
                    Allure.getLifecycle().getCurrentTestCase();

            if (uuid.isEmpty()) {
                log.warn("No active Allure test case. Skipping attachment.");
                return;
            }

            byte[] screenshot =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);

            Allure.getLifecycle().addAttachment(
                    "Failure Screenshot",
                    "image/png",
                    "png",
                    new ByteArrayInputStream(screenshot)
            );

            log.info("Screenshot attached to Allure (Lifecycle API).");

        } catch (Exception e) {
            log.error("Failed to attach screenshot", e);
        }
    }
    @Attachment(value = "Failure Screenshot", type = "image/png")
    private static byte[] saveScreenshot(byte[] screenshot) {
        return screenshot;
    }

    /* ================= PAGE SOURCE ================= */

    public static void attachPageSource(WebDriver driver) {

        if (!ReportingConfig.isEnabled("report.pageSource.enabled"))
            return;

        try {

            String source = driver.getPageSource();
            savePageSource(source);

            log.info("Page source attached to Allure.");

        } catch (Exception e) {

            log.error("Failed to attach page source", e);
        }
    }

    @Attachment(value = "Page Source", type = "text/html")
    private static String savePageSource(String source) {
        return source;
    }

    /* ================= CONSOLE LOGS ================= */

    public static void attachConsoleLogs(WebDriver driver) {

        if (!ReportingConfig.isEnabled("report.consoleLogs.enabled"))
            return;

        try {

            LogEntries logs =
                    driver.manage().logs().get(LogType.BROWSER);

            StringBuilder builder = new StringBuilder();

            for (LogEntry entry : logs) {
                builder.append(entry.getLevel())
                        .append(" - ")
                        .append(entry.getMessage())
                        .append("\n");
            }

            saveConsoleLogs(builder.toString());

        } catch (Exception e) {

            log.warn("Console logs not supported.");
        }
    }

    @Attachment(value = "Browser Console Logs", type = "text/plain")
    private static String saveConsoleLogs(String logs) {
        return logs;
    }
}