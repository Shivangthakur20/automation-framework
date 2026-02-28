package core.notification;

import core.metrics.ExecutionSummary;

public final class NotificationManager {

    private NotificationManager() {}

    public static void notify(ExecutionSummary summary) {

        SlackNotificationService.send(summary);
    }
}