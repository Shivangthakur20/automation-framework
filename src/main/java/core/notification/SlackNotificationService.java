package core.notification;

import core.metrics.ExecutionSummary;
import core.config.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SlackNotificationService {

    private static final Logger log =
            LogManager.getLogger(SlackNotificationService.class);

    private SlackNotificationService() {}

    public static void send(ExecutionSummary summary) {

        String webhook = ConfigReader.get("slack.webhook");

        if (webhook == null || webhook.isEmpty()) {
            log.warn("Slack webhook not configured.");
            return;
        }

        String message =
                "Suite: " + summary.getSuiteName() +
                        "\nEnvironment: " + summary.getEnvironment() +
                        "\nTotal: " + summary.getTotal() +
                        "\nPassed: " + summary.getPassed() +
                        "\nFailed: " + summary.getFailed() +
                        "\nDuration(ms): " + summary.getDurationMs();

        // FIXED LINE
        SlackNotifier.send(message);

        log.info("Slack notification sent.");
    }
}