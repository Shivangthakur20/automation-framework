package core.notification;

import core.config.ConfigReader;
import core.metrics.ExecutionSummary;

import java.util.List;

public class NotificationManager {

    private final List<NotificationService> services;

    public NotificationManager(List<NotificationService> services) {
        this.services = services;
    }

    public void process(ExecutionSummary summary) {

        if (!ConfigReader.get("notification.enabled")
                .equalsIgnoreCase("true")) {
            return;
        }

        if (ConfigReader.get("notification.mode")
                .equalsIgnoreCase("ci")) {
            return; // CI handles notification
        }

        for (NotificationService service : services) {
            if (service.isEnabled()) {
                service.notify(summary);
            }
        }
    }
}