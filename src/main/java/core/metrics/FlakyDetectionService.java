package core.metrics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlakyDetectionService {

    private static final Logger log =
            LogManager.getLogger(FlakyDetectionService.class);

    public static void analyzeFlakiness(
            String suiteName,
            int historyCount,
            double threshold
    ) {

        List<ExecutionSummary> runs =
                ExecutionHistoryManager
                        .getLastRuns(suiteName, historyCount);

        if (runs.size() < 2) {
            log.info("Flaky detection skipped — insufficient history.");
            return;
        }

        Map<String, Integer> failureCounts = new HashMap<>();
        Map<String, Integer> appearanceCounts = new HashMap<>();

        for (ExecutionSummary run : runs) {

            for (Map.Entry<String, TestResultInfo> entry :
                    run.getTests().entrySet()) {

                String testName = entry.getKey();
                TestResultInfo result = entry.getValue();

                appearanceCounts.merge(
                        testName,
                        1,
                        Integer::sum
                );

                if (!result.isPassed() && !result.isSkipped()) {

                    failureCounts.merge(
                            testName,
                            1,
                            Integer::sum
                    );
                }
            }
        }

        log.info("========== FLAKY DETECTION ==========");

        for (String test : appearanceCounts.keySet()) {

            int failures =
                    failureCounts.getOrDefault(test, 0);

            int appearances =
                    appearanceCounts.get(test);

            double ratio =
                    (double) failures / appearances;

            if (ratio > threshold && ratio < 1.0) {

                log.warn(
                        "Flaky Test Detected: {} | Failure Ratio: {}",
                        test,
                        ratio
                );

                // Add to quarantine so future runs can skip it
                FlakyQuarantineManager.add(test);
            }
        }

        log.info("======================================");
    }
}
