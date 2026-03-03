package core.metrics;

import core.config.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages a simple on-disk quarantine list of flaky tests.
 *
 * Format: one test name per line, stored under:
 *   execution-history/{env}/quarantine.txt
 *
 * The test name is the same key used in MetricsCollector
 * (currently the method name).
 */
public final class FlakyQuarantineManager {

    private FlakyQuarantineManager() {
    }

    private static File resolveFile() {
        String env = ConfigReader.getOrDefault("env", "default");
        return new File(
                ExecutionHistoryManager.HISTORY_BASE_DIR
                        + env + "/quarantine.txt");
    }

    public static synchronized void add(String testName) {
        try {
            Set<String> current = loadAll();
            if (current.add(testName)) {
                writeAll(current);
            }
        } catch (IOException ignored) {
        }
    }

    public static synchronized boolean isQuarantined(String testName) {
        try {
            return loadAll().contains(testName);
        } catch (IOException e) {
            return false;
        }
    }

    private static Set<String> loadAll() throws IOException {
        File file = resolveFile();
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            return new HashSet<>();
        }
        List<String> lines = Files.readAllLines(file.toPath());
        Set<String> set = new HashSet<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                set.add(trimmed);
            }
        }
        return set;
    }

    private static void writeAll(Set<String> entries) throws IOException {
        File file = resolveFile();
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
        }
        List<String> sorted = entries.stream()
                .sorted()
                .collect(Collectors.toList());
        Files.write(file.toPath(), sorted);
    }

    public static Set<String> readOnly() {
        try {
            return Collections.unmodifiableSet(loadAll());
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }
}

