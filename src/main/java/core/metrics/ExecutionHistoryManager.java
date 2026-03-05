package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ExecutionHistoryManager {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    // Base directory for persistent execution history
    // (outside Maven target so it survives `mvn clean`)
    public static final String HISTORY_BASE_DIR =
            "execution-history/";

    /* ================= SAVE EXECUTION ================= */

    public static void save(ExecutionSummary summary)
            throws IOException {

        if (!Boolean.parseBoolean(
                ConfigReader.getOrDefault("trend.enabled", "false"))) {
            return;
        }

        String suite = summary.getSuiteName();

        String env =
                Optional.ofNullable(summary.getEnvironment())
                        .filter(s -> !s.isBlank())
                        .orElseGet(() ->
                                ConfigReader.getOrDefault("env", "default"));

        File dir = new File(
                HISTORY_BASE_DIR + env + File.separator + suite
        );

        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp =
                LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("yyyy-MM-dd_HH-mm-ss"));

        File file = new File(
                dir,
                "execution-" + timestamp + ".json"
        );

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(file, summary);

        cleanupOldReports(dir);
    }

    /* ================= LOAD LAST RUNS ================= */

    public static List<ExecutionSummary> getLastRuns(
            String suite,
            int limit) {

        String env = ConfigReader.getOrDefault("env", "default");

        File dir = new File(
                HISTORY_BASE_DIR + env + File.separator + suite
        );

        if (!dir.exists()) {
            return Collections.emptyList();
        }

        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }

        Arrays.sort(files,
                Comparator.comparingLong(File::lastModified)
                        .reversed());

        return Arrays.stream(files)
                .limit(limit)
                .map(file -> {
                    try {
                        return mapper.readValue(
                                file,
                                ExecutionSummary.class);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /* ================= CLEANUP OLD FILES ================= */

    private static void cleanupOldReports(File dir)
            throws IOException {

        int limit = Integer.parseInt(
                ConfigReader.getOrDefault(
                        "trend.history.count", "5"));

        File[] files = dir.listFiles();

        if (files == null || files.length <= limit) {
            return;
        }

        Arrays.sort(files,
                Comparator.comparingLong(File::lastModified)
                        .reversed());

        for (int i = limit; i < files.length; i++) {
            Files.deleteIfExists(files[i].toPath());
        }
    }
}