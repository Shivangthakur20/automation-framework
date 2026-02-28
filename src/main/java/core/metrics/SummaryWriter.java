package core.metrics;

import core.config.ConfigReader;
import core.reporting.ReportingConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class SummaryWriter {

    private static final Logger log =
            LogManager.getLogger(SummaryWriter.class);

    public static void write(ExecutionSummary summary) {

        try {

            ExecutionHistoryManager.save(summary);

            writeEnvironment();   // <-- add this

            log.info("Execution summary persisted successfully.");

        } catch (Exception e) {

            log.error("Failed to persist execution summary", e);
        }
    }

    private static void writeEnvironment() {

        if (!ReportingConfig.isEnabled("allure.environment.enabled"))
            return;

        try {

            Path path = Path.of(
                    "target/allure-results/environment.properties"
            );

            String content =
                    "Environment=" + ConfigReader.get("env") + "\n" +
                            "Browser=" + ConfigReader.get("browser") + "\n" +
                            "RunMode=" + ConfigReader.get("run.mode");

            Files.writeString(path, content);

        } catch (Exception ignored) {}
    }
}