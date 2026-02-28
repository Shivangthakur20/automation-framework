package core.metrics;

import java.util.Map;

public class ExecutionSummary {

    private final String suiteName;
    private final String environment;
    private final int total;
    private final int passed;
    private final int failed;
    private final int skipped;
    private final long durationMs;
    private final Map<String, TestResultInfo> tests;

    public ExecutionSummary(String suiteName,
                            String environment,
                            int total,
                            int passed,
                            int failed,
                            int skipped,
                            long durationMs,
                            Map<String, TestResultInfo> tests) {

        this.suiteName = suiteName;
        this.environment = environment;
        this.total = total;
        this.passed = passed;
        this.failed = failed;
        this.skipped = skipped;
        this.durationMs = durationMs;
        this.tests = tests;
    }

    public String getSuiteName() { return suiteName; }
    public String getEnvironment() { return environment; }
    public int getTotal() { return total; }
    public int getPassed() { return passed; }
    public int getFailed() { return failed; }
    public int getSkipped() { return skipped; }
    public long getDurationMs() { return durationMs; }
    public Map<String, TestResultInfo> getTests() { return tests; }
    public int getFailures() {
        return failed;
    }

    public long getDuration() {
        return durationMs;
    }
}