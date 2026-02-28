package core.notification;

import core.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SlackNotifier {

    private static final Logger log =
            LogManager.getLogger(SlackNotifier.class);

    public static void send(String message) {

        try {

            String webhookUrl =
                    ConfigReader.get("slack.webhook.url");

            URL url = new URL(webhookUrl);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type",
                    "application/json");

            String payload =
                    "{ \"text\": \"" +
                            message.replace("\"", "\\\"") +
                            "\" }";

            try (OutputStream os =
                         connection.getOutputStream()) {

                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode =
                    connection.getResponseCode();

            if (responseCode != 200) {

                log.error(
                        "Slack notification failed. Response code: {}",
                        responseCode);
            }

        } catch (Exception e) {

            log.error("Error sending Slack notification", e);
        }
    }
}