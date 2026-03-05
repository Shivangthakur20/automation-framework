package core.reporting;

import java.io.InputStream;
import java.util.Properties;

public final class ReportingConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream input =
                     ReportingConfig.class
                             .getClassLoader()
                             .getResourceAsStream("reporting.properties")) {

            if (input != null) {
                props.load(input);
            }

        } catch (Exception ignored) {}
    }

    private ReportingConfig() {}

    public static boolean isEnabled(String key) {
        return Boolean.parseBoolean(
                props.getProperty(key, "false")
        );
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}