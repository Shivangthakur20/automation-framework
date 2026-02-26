package constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}

    public static final String TARGET_DIR = "target/";

    public static final String LOGS_DIR =
            TARGET_DIR + "logs/";

    public static final String METRICS_DIR =
            TARGET_DIR + "metrics/";

    public static final String ALLURE_RESULTS_DIR =
            TARGET_DIR + "allure-results/";

    public static final String VIDEOS_DIR =
            TARGET_DIR + "videos/";

    public static final String EXECUTION_SUMMARY_FILE =
            TARGET_DIR + "execution-summary.json";

    public static final int DEFAULT_WAIT = 10;
}