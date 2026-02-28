package core.config;

import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     ConfigReader.class.getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties not found");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private ConfigReader() {}

    // ========================
    // STRING
    // ========================

    public static String get(String key) {
        return System.getProperty(
                key,
                properties.getProperty(key)
        );
    }

    public static String getOrDefault(String key, String defaultValue) {
        return System.getProperty(
                key,
                properties.getProperty(key, defaultValue)
        );
    }

    // ========================
    // INT
    // ========================

    public static int getInt(String key, int defaultValue) {

        String value = get(key);

        if (value == null || value.isEmpty())
            return defaultValue;

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid integer for key: " + key);
        }
    }

    // ========================
    // BOOLEAN
    // ========================

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getOrDefault(key, "false"));
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(
                getOrDefault(key, String.valueOf(defaultValue))
        );
    }
}