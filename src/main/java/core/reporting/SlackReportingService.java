package core.reporting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public final class SlackReportingService {

    private static final Logger log =
            LogManager.getLogger(SlackReportingService.class);

    private SlackReportingService() {}

    public static void notifyFailure(String message) {

        if (!ReportingConfig.isEnabled("slack.enabled"))
            return;

        try {

            URL url = new URL(
                    ReportingConfig.get("slack.webhook.url")
            );

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String payload =
                    "{\"text\":\"" + message + "\"}";

            try (OutputStream os =
                         conn.getOutputStream()) {

                os.write(payload.getBytes());
            }

            conn.getResponseCode();

            log.info("Slack notification sent.");

        } catch (Exception e) {
            log.error("Slack notification failed", e);
        }
    }
}