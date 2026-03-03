package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.config.ConfigReader;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class TrendAnalyzer {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    public static ExecutionSummary getPrevious(
            String suite) {

        String env = ConfigReader.getOrDefault("env", "default");

        File dir = new File(
                ExecutionHistoryManager.HISTORY_BASE_DIR
                        + env + File.separator + suite);

        if (!dir.exists()) {
            return null;
        }

        File[] files = dir.listFiles();

        if (files == null || files.length < 2) {
            return null;
        }

        Arrays.sort(files,
                Comparator.comparingLong(File::lastModified)
                        .reversed());

        try {
            return mapper.readValue(
                    files[1],
                    ExecutionSummary.class);
        } catch (Exception e) {
            return null;
        }
    }
}