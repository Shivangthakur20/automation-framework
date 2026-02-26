package core.flaky;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class FlakyAnalyzer {

    private static final Map<String, Integer> flakyCounts =
            new ConcurrentHashMap<>();

    private static final int FLAKY_THRESHOLD = 3;

    public static void recordRetry(String testName) {

        flakyCounts.merge(testName, 1, Integer::sum);
    }

    public static boolean isFlaky(String testName) {

        return flakyCounts.getOrDefault(testName, 0)
                >= FLAKY_THRESHOLD;
    }
}