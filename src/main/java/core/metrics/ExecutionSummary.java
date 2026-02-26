package core.metrics;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ExecutionSummary {

    private String executionId;
    private String suite;
    private String environment;

    private int total;
    private int passed;
    private int failed;
    private int skipped;

    private double passRate;
    private long durationMs;

    private LocalDateTime timestamp;

    private String buildNumber;     // Optional (CI)
    private String buildUrl;        // Optional (CI)

    private List<FailureInfo> failures;

    public ExecutionSummary() {
        // Needed for Jackson
    }

    public ExecutionSummary(String suite,
                            String environment,
                            int total,
                            int passed,
                            int failed,
                            int skipped,
                            long durationMs,
                            List<FailureInfo> failures) {

        this.executionId = UUID.randomUUID().toString();
        this.suite = suite;
        this.environment = environment;
        this.total = total;
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
        this.durationMs = durationMs;
        this.timestamp = LocalDateTime.now();
        this.failures = failures;

        calculatePassRate();
        loadBuildMetadata();
    }

    private void calculatePassRate() {
        if (total == 0) {
            this.passRate = 0;
        } else {
            this.passRate = (passed * 100.0) / total;
        }
    }

    private void loadBuildMetadata() {

        // Jenkins automatically exposes these as ENV
        this.buildNumber = System.getenv("BUILD_NUMBER");
        this.buildUrl = System.getenv("BUILD_URL");
    }

    /* ================= GETTERS ================= */

    public String getExecutionId() {
        return executionId;
    }

    public String getSuite() {
        return suite;
    }

    public String getEnvironment() {
        return environment;
    }

    public int getTotal() {
        return total;
    }

    public int getPassed() {
        return passed;
    }

    public int getFailed() {
        return failed;
    }

    public int getSkipped() {
        return skipped;
    }

    public double getPassRate() {
        return passRate;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getBuildUrl() {
        return buildUrl;
    }

    public List<FailureInfo> getFailures() {
        return failures;
    }
}