package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SummaryWriter {

    private static final Logger log =
            LogManager.getLogger(SummaryWriter.class);
    public static void write(ExecutionSummary summary) {

        try {

            Path path =
                    Paths.get("target", "execution-summary.json");

            Files.createDirectories(path.getParent());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(path.toString()), summary);

        } catch (Exception e) {
           log.warn(e.getMessage());
        }
    }
}