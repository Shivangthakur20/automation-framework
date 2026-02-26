package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MetricsPersistence {

    private static final String METRICS_DIR = "metrics/";

    public static void persist(Map<String, Object> data) {

        try {
            File dir = new File(METRICS_DIR);
            if (!dir.exists()) dir.mkdirs();

            String timestamp =
                    LocalDateTime.now()
                            .format(DateTimeFormatter
                                    .ofPattern("yyyy-MM-dd_HH-mm-ss"));

            File file = new File(
                    METRICS_DIR + "execution-" + timestamp + ".json"
            );

            new ObjectMapper().writeValue(file, data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}