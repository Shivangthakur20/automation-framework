package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import constants.FrameworkConstants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public final class MetricsPersistence {

    private MetricsPersistence() {}

    public static void persist(Map<String, Object> data) {

        try {

            Files.createDirectories(
                    Paths.get(FrameworkConstants.METRICS_DIR)
            );

            String timestamp =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter
                                            .ofPattern(
                                                    "yyyy-MM-dd_HH-mm-ss"
                                            )
                            );

            String fileName =
                    FrameworkConstants.METRICS_DIR +
                            "execution-" +
                            timestamp +
                            ".json";

            ObjectMapper mapper =
                    new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(fileName),
                            data
                    );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to persist metrics", e);
        }
    }
}