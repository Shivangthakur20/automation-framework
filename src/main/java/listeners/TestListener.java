package listeners;

import core.driver.DriverManager;
import core.metrics.ExecutionSummary;
import core.metrics.MetricsCollector;
import core.metrics.SummaryWriter;
import core.notification.NotificationManager;
import core.reporting.AllureAttachmentService;
import core.reporting.ReportingConfig;
import core.config.ConfigReader;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.testng.*;

public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger log =
            LogManager.getLogger(TestListener.class);

    private MetricsCollector metricsCollector;

    private static final ThreadLocal<Long> testStartTime =
            new ThreadLocal<>();

    /* ================= SUITE LEVEL ================= */

    @Override
    public void onStart(ISuite suite) {

        metricsCollector = new MetricsCollector();
        metricsCollector.startSuite();

        log.info("SUITE STARTED: {}", suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {

        String environment = ConfigReader.get("env");

        if (environment == null || environment.isEmpty()) {
            environment = "local";
        }

        ExecutionSummary summary =
                metricsCollector.buildSummary(
                        suite.getName(),
                        environment
                );

        SummaryWriter.write(summary);

        if (ReportingConfig.isEnabled("report.slack.enabled")) {
            NotificationManager.notify(summary);
        }

        log.info("SUITE FINISHED: {}", suite.getName());
    }

    /* ================= TEST LEVEL ================= */

    @Override
    public void onTestStart(ITestResult result) {
        testStartTime.set(System.currentTimeMillis());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        long duration = getDuration();

        metricsCollector.recordSuccess(
                result.getMethod().getMethodName(),
                duration,
                result.getMethod().getCurrentInvocationCount() - 1
        );

        testStartTime.remove();
    }


    @Override
    public void onTestFailure(ITestResult result) {

        WebDriver driver = DriverManager.getDriver();

        if (driver != null) {
            AllureAttachmentService.attachScreenshot(driver);
            AllureAttachmentService.attachPageSource(driver);
        }

        long duration = getDuration();

        metricsCollector.recordFailure(
                result.getMethod().getMethodName(),
                duration,
                result.getMethod().getCurrentInvocationCount() - 1
        );

        testStartTime.remove();
    }
    @Override
    public void onTestSkipped(ITestResult result) {

        metricsCollector.recordSkipped(
                result.getMethod().getMethodName()
        );

        testStartTime.remove();
    }

    /* ================= UTIL ================= */

    private long getDuration() {

        Long start = testStartTime.get();
        if (start == null) return 0;

        return System.currentTimeMillis() - start;
    }

    private void attachFailureArtifacts() {

        WebDriver driver = DriverManager.getDriver();

        if (driver == null) {
            log.warn("Driver is null. Cannot attach artifacts.");
            return;
        }

        if (ReportingConfig.isEnabled("report.screenshot.enabled")) {
            AllureAttachmentService.attachScreenshot(driver);
        }

        if (ReportingConfig.isEnabled("report.pagesource.enabled")) {
            AllureAttachmentService.attachPageSource(driver);
        }

        if (ReportingConfig.isEnabled("report.consoleLogs.enabled")) {
            AllureAttachmentService.attachConsoleLogs(driver);
        }
    }


}