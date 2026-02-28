package core.metrics;

import java.util.List;

public class TrendResult {

    private int failureDelta;
    private long durationDelta;
    private List<String> newFailures;

    public TrendResult(int failureDelta,
                       long durationDelta,
                       List<String> newFailures) {
        this.failureDelta = failureDelta;
        this.durationDelta = durationDelta;
        this.newFailures = newFailures;
    }

    public int getFailureDelta() {
        return failureDelta;
    }

    public long getDurationDelta() {
        return durationDelta;
    }

    public List<String> getNewFailures() {
        return newFailures;
    }
}