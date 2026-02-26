package core.flaky;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Set;

public class FlakyQuarantineManager {

    private static final Set<String> quarantined =
            load();

    private static Set<String> load() {

        try {
            return new ObjectMapper().readValue(
                    new File("flaky-tests.json"),
                    new TypeReference<Set<String>>() {});
        } catch (Exception e) {
            return Set.of();
        }
    }

    public static boolean isQuarantined(String testName) {
        return quarantined.contains(testName);
    }
}