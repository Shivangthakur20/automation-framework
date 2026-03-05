package core.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

public final class MetricsCollector {

    private final Map<String, TestResultInfo> testResults =
            new ConcurrentHashMap<>();

    private final AtomicInteger passedCount = new AtomicInteger();
    private final AtomicInteger failedCount = new AtomicInteger();
    private final AtomicInteger skippedCount = new AtomicInteger();

    private long suiteStartTime;

    public void startSuite() {
        suiteStartTime = System.currentTimeMillis();
    }

    public void recordSuccess(String testName, long duration, int retries) {
        passedCount.incrementAndGet();
        testResults.put(testName,
                new TestResultInfo(true, false, duration, retries));
    }

    public void recordFailure(String testName, long duration, int retries) {
        failedCount.incrementAndGet();
        testResults.put(testName,
                new TestResultInfo(false, false, duration, retries));
    }

    public void recordSkipped(String testName) {
        skippedCount.incrementAndGet();
        testResults.put(testName,
                new TestResultInfo(false, true, 0, 0));
    }

    public ExecutionSummary buildSummary(String suiteName, String environment) {

        long duration =
                System.currentTimeMillis() - suiteStartTime;

        return new ExecutionSummary(
                suiteName,
                environment,
                passedCount.get() + failedCount.get() + skippedCount.get(),
                passedCount.get(),
                failedCount.get(),
                skippedCount.get(),
                duration,
                testResults
        );
    }
}