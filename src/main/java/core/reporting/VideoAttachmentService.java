package core.reporting;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class VideoAttachmentService {

    private static final Logger log =
            LogManager.getLogger(VideoAttachmentService.class);

    private VideoAttachmentService() {}

    public static void attachVideo(String sessionId) {

        if (!ReportingConfig.isEnabled("report.video.enabled"))
            return;

        if (sessionId == null || sessionId.isEmpty()) {
            log.warn("Session ID is null. Video cannot be attached.");
            return;
        }

        try {

            String videoUrl =
                    "http://localhost:4444/video/" + sessionId + ".mp4";

            Allure.addAttachment(
                    "Execution Video",
                    "text/plain",
                    videoUrl
            );

            log.info("Video attachment added to Allure: {}", videoUrl);

        } catch (Exception e) {
            log.error("Failed to attach video", e);
        }
    }
}