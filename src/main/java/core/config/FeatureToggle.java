package core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum FeatureToggle {

    GRID("grid.enabled"),
    VIDEO("video.enabled"),
    METRICS("metrics.enabled"),
    FLAKY("flaky.enabled"),
    SLACK("slack.enabled"),
    DEVTOOLS("devtools.enabled");

    private static final Logger log =
            LogManager.getLogger(FeatureToggle.class);

    private final String key;

    FeatureToggle(String key) {
        this.key = key;
    }

    public boolean isEnabled() {

        boolean enabled = ConfigReader.getBoolean(key);

        log.info("Feature '{}' enabled = {}", key, enabled);

        return enabled;
    }
}