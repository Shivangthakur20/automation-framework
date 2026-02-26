package core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Logger log =
            LogManager.getLogger(ConfigReader.class);

    private static final Properties properties =
            new Properties();

    private static String activeEnv;

    static {
        try {

            // 1️⃣ Determine Environment (System > ENV > default QA)
            activeEnv = System.getProperty(
                    "env",
                    System.getenv().getOrDefault("env", "qa")
            );

            log.info("Active environment: {}", activeEnv);

            // 2️⃣ Load Base Config
            loadFile("config/base.properties");

            // 3️⃣ Load Environment Config
            loadFile("config/" + activeEnv + ".properties");

            // 4️⃣ Override With ENV Variables (Docker)
            overrideWithEnvironmentVariables();

            // 5️⃣ Override With System Properties (-D)
            overrideWithSystemProperties();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load configuration", e);
        }
    }

    private static void loadFile(String fileName)
            throws Exception {

        InputStream input =
                ConfigReader.class
                        .getClassLoader()
                        .getResourceAsStream(fileName);

        if (input == null) {
            log.warn("Config file not found: {}", fileName);
            return;
        }

        properties.load(input);
        log.info("Loaded config file: {}", fileName);
    }

    private static void overrideWithSystemProperties() {

        System.getProperties().forEach((key, value) -> {

            if (properties.containsKey(key)) {

                log.info("Overriding '{}' with system property '{}'",
                        key, value);

                properties.put(key, value);
            }
        });
    }

    private static void overrideWithEnvironmentVariables() {

        System.getenv().forEach((key, value) -> {

            if (properties.containsKey(key)) {

                log.info("Overriding '{}' with ENV variable '{}'",
                        key, value);

                properties.put(key, value);
            }
        });
    }

    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {
            log.warn("Config key '{}' not found", key);
        }

        return value;
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String getActiveEnv() {
        return activeEnv;
    }
}