package core.metrics;

import constants.FrameworkConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.HashMap;

public final class MetricsCollector {

    private static final Map<String, Object> testResults =
            new ConcurrentHashMap<>();

    private static final AtomicInteger passedCount =
            new AtomicInteger();

    private static final AtomicInteger failedCount =
            new AtomicInteger();

    private static final AtomicInteger skippedCount =
            new AtomicInteger();

    private static long suiteStartTime;

    private MetricsCollector() {}

    public static void startSuite() {
        suiteStartTime = System.currentTimeMillis();
    }

    public static void recordTest(String name,
                                  boolean passed,
                                  long duration,
                                  int retryCount,
                                  boolean skipped) {

        Map<String, Object> testData = new HashMap<>();
        testData.put("passed", passed);
        testData.put("durationMs", duration);
        testData.put("retries", retryCount);
        testData.put("skipped", skipped);

        testResults.put(name, testData);

        if (skipped) {
            skippedCount.incrementAndGet();
        } else if (passed) {
            passedCount.incrementAndGet();
        } else {
            failedCount.incrementAndGet();
        }
    }

    public static void endSuite(String suiteName,
                                String environment) {

        long totalExecutionTime =
                System.currentTimeMillis() - suiteStartTime;

        Map<String, Object> summary = new HashMap<>();
        summary.put("suite", suiteName);
        summary.put("environment", environment);
        summary.put("totalTests",
                passedCount.get()
                        + failedCount.get()
                        + skippedCount.get());
        summary.put("passed", passedCount.get());
        summary.put("failed", failedCount.get());
        summary.put("skipped", skippedCount.get());
        summary.put("totalExecutionTimeMs",
                totalExecutionTime);
        summary.put("tests", testResults);

        MetricsPersistence.persist(summary);
    }
}