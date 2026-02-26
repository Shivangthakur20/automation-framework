package core.notification;

import core.metrics.ExecutionSummary;

public interface NotificationService {

    boolean isEnabled();

    void notify(ExecutionSummary summary);
}