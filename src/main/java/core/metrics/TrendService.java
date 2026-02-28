package core.metrics;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrendService {

    public static TrendResult analyze(
            ExecutionSummary current,
            ExecutionSummary previous) {

        int failureDelta =
                current.getFailed() - previous.getFailed();

        long durationDelta =
                current.getDurationMs() - previous.getDurationMs();

        List<String> currentFailed =
                current.getTests()
                        .entrySet()
                        .stream()
                        .filter(entry ->
                                !entry.getValue().isPassed()
                                        && !entry.getValue().isSkipped())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        List<String> previousFailed =
                previous.getTests()
                        .entrySet()
                        .stream()
                        .filter(entry ->
                                !entry.getValue().isPassed()
                                        && !entry.getValue().isSkipped())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        List<String> newFailures =
                currentFailed.stream()
                        .filter(t -> !previousFailed.contains(t))
                        .collect(Collectors.toList());

        return new TrendResult(
                failureDelta,
                durationDelta,
                newFailures
        );
    }
}