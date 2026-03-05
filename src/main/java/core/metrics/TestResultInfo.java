package core.metrics;

public class TestResultInfo {

    private final boolean passed;
    private final boolean skipped;
    private final long durationMs;
    private final int retries;

    public TestResultInfo(boolean passed,
                          boolean skipped,
                          long durationMs,
                          int retries) {

        this.passed = passed;
        this.skipped = skipped;
        this.durationMs = durationMs;
        this.retries = retries;
    }

    public boolean isPassed() {
        return passed;
    }

    public boolean isSkipped() {
        return skipped;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public int getRetries() {
        return retries;
    }
}