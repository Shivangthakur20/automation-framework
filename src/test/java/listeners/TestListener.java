package listeners;

import core.config.ConfigReader;
import core.metrics.ExecutionSummary;
import core.metrics.FailureInfo;
import core.metrics.SummaryWriter;
import core.notification.NotificationManager;
import core.notification.SlackNotificationService;
import core.notification.SlackNotifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestListener implements ITestListener {

    private static final Logger log =
            LogManager.getLogger(TestListener.class);

    private long suiteStartTime;

    private static final List<FailureInfo> failures =
            Collections.synchronizedList(new ArrayList<>());

    /* ============================================================
       SUITE START
       ============================================================ */
    @Override
    public void onStart(ITestContext context) {

        suiteStartTime = System.currentTimeMillis();

        log.info("========== SUITE STARTED ==========");
        log.info("Suite Name: {}",
                ConfigReader.get("suite.name"));
        log.info("Environment: {}",
                ConfigReader.getActiveEnv());
    }

    /* ============================================================
       TEST FAILURE
       ============================================================ */
    @Override
    public void onTestFailure(ITestResult result) {

        String name =
                result.getMethod().getMethodName();

        String error =
                result.getThrowable() != null
                        ? result.getThrowable().getMessage()
                        : "Unknown error";

        String stackTrace =
                result.getThrowable() != null
                        ? getStackTrace(result.getThrowable())
                        : "";

        failures.add(
                new FailureInfo(name, error, stackTrace)
        );

        log.error("Test '{}' FAILED", name);
        log.error("Reason: {}", error);

        // Optional Per-Test Slack Mode
        if ("perTest".equalsIgnoreCase(
                ConfigReader.get("notification.mode"))) {

            SlackNotifier.notifyFailure(
                    name,
                    error,
                    ConfigReader.getActiveEnv()
            );
        }
    }

    /* ============================================================
       TEST SKIPPED
       ============================================================ */
    @Override
    public void onTestSkipped(ITestResult result) {

        log.warn("Test '{}' SKIPPED",
                result.getMethod().getMethodName());
    }

    /* ============================================================
       SUITE FINISH
       ============================================================ */
    @Override
    public void onFinish(ITestContext context) {

        long duration =
                System.currentTimeMillis() - suiteStartTime;

        int total =
                context.getAllTestMethods().length;

        int passed =
                context.getPassedTests().size();

        int failed =
                context.getFailedTests().size();

        int skipped =
                context.getSkippedTests().size();

        log.info("========== SUITE FINISHED ==========");
        log.info("Total: {}", total);
        log.info("Passed: {}", passed);
        log.info("Failed: {}", failed);
        log.info("Skipped: {}", skipped);
        log.info("Duration: {} ms", duration);

        ExecutionSummary summary =
                new ExecutionSummary(
                        ConfigReader.get("suite.name"),
                        ConfigReader.getActiveEnv(),
                        total,
                        passed,
                        failed,
                        skipped,
                        duration,
                        failures
                );

        // Persist summary for CI / Dashboard
        SummaryWriter.write(summary);

        // Batch Notification Mode
        if ("framework".equalsIgnoreCase(
                ConfigReader.get("notification.mode"))) {

            NotificationManager manager =
                    new NotificationManager(
                            List.of(
                                    new SlackNotificationService()
                            )
                    );

            manager.process(summary);
        }

        failures.clear();
    }

    /* ============================================================
       UTILITY METHOD
       ============================================================ */
    private String getStackTrace(Throwable throwable) {

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement element :
                throwable.getStackTrace()) {

            sb.append(element.toString())
                    .append("\\n");
        }

        return sb.toString();
    }
}