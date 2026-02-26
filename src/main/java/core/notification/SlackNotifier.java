package core.notification;

import core.metrics.ExecutionSummary;
import core.security.SecretManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SlackNotifier {

    private static final Logger log =
            LogManager.getLogger(SlackNotifier.class);

    private static final int TIMEOUT = 10000;

    /* ============================================================
       PER TEST FAILURE NOTIFICATION
       ============================================================ */
    public static void notifyFailure(String testName,
                                     String errorMessage,
                                     String environment) {

        String message =
                ":x: *Test Failed*\\n"
                        + "*Environment:* " + environment + "\\n"
                        + "*Test:* " + testName + "\\n"
                        + "*Error:* " + errorMessage;

        sendMessage(message);
    }

    /* ============================================================
       BATCH EXECUTION SUMMARY
       ============================================================ */
    public static void send(ExecutionSummary summary) {

        String message = buildSummaryMessage(summary);

        sendMessage(message);
    }

    /* ============================================================
       CORE HTTP SENDER
       ============================================================ */
    private static void sendMessage(String textMessage) {

        try {

            String webhook =
                    SecretManager.getSecret("SLACK_WEBHOOK");

            URL url = new URL(webhook);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);
            conn.setDoOutput(true);

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json; charset=UTF-8"
            );

            String payload =
                    "{ \"text\": \"" + textMessage + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                log.info("Slack notification sent successfully.");
            } else {
                log.error("Slack notification failed. HTTP code: {}",
                        responseCode);
            }

        } catch (Exception e) {
            log.error("Slack notification error: {}",
                    e.getMessage());
        }
    }

    /* ============================================================
       BUILD SUMMARY MESSAGE
       ============================================================ */
    private static String buildSummaryMessage(
            ExecutionSummary summary) {

        StringBuilder message =
                new StringBuilder();

        message.append("🚀 *Execution Summary*\\n")
                .append("*Suite:* ")
                .append(summary.getSuite())
                .append("\\n")

                .append("*Environment:* ")
                .append(summary.getEnvironment())
                .append("\\n")

                .append("*Total:* ")
                .append(summary.getTotal())
                .append("\\n")

                .append("*Passed:* ")
                .append(summary.getPassed())
                .append("\\n")

                .append("*Failed:* ")
                .append(summary.getFailed())
                .append("\\n")

                .append("*Skipped:* ")
                .append(summary.getSkipped())
                .append("\\n")

                .append("*Pass Rate:* ")
                .append(String.format("%.2f",
                        summary.getPassRate()))
                .append("%\\n")

                .append("*Duration:* ")
                .append(summary.getDurationMs())
                .append(" ms\\n");

        if (summary.getFailures() != null
                && !summary.getFailures().isEmpty()) {

            message.append("\\n*Failures:*\\n");

            summary.getFailures().forEach(f ->
                    message.append("• ")
                            .append(f.getTestName())
                            .append(" → ")
                            .append(f.getErrorMessage())
                            .append("\\n")
            );
        }

        return message.toString();
    }
}