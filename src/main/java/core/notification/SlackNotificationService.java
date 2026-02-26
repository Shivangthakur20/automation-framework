package core.notification;

import core.config.ConfigReader;
import core.metrics.ExecutionSummary;

public class SlackNotificationService
        implements NotificationService {

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(
                ConfigReader.get("slack.enabled"));
    }

    @Override
    public void notify(ExecutionSummary summary) {

        if (!isEnabled()) return;

        boolean onlyOnFailure =
                Boolean.parseBoolean(
                        ConfigReader.get("notification.onlyOnFailure"));

        int minPassRate =
                Integer.parseInt(
                        ConfigReader.get("notification.minPassRate"));

        if (onlyOnFailure && summary.getFailed() == 0)
            return;

        if (summary.getPassRate() >= minPassRate)
            return;

        SlackNotifier.send(summary);
    }
}