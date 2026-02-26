package core.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SummaryWriter {

    public static void write(ExecutionSummary summary) {

        try {

            Path path =
                    Paths.get("target", "execution-summary.json");

            Files.createDirectories(path.getParent());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(path.toString()), summary);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}