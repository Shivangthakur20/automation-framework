package core.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class MetricsCollector {

    private static final Map<String, Object> results =
            new ConcurrentHashMap<>();

    private static long suiteStart;

    public static void startSuite() {
        suiteStart = System.currentTimeMillis();
    }

    public static void recordTest(String name,
                                  boolean passed,
                                  long duration,
                                  int retryCount) {

        results.put(name, Map.of(
                "passed", passed,
                "duration", duration,
                "retries", retryCount
        ));
    }

    public static void endSuite() {

        long totalTime =
                System.currentTimeMillis() - suiteStart;

        results.put("totalExecutionTime", totalTime);

        MetricsPersistence.persist(results);
    }
}