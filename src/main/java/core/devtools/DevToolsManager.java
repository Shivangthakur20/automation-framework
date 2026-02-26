package core.devtools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;

import java.util.Optional;

public class DevToolsManager {

    private static final Logger log =
            LogManager.getLogger(DevToolsManager.class);
    public static void enableNetworkCapture(ChromeDriver driver) {

        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        ));

        devTools.addListener(Network.responseReceived(),
                response -> {
                    log.info(
                            "API: " +
                                    response.getResponse().getUrl() +
                                    " Status: " +
                                    response.getResponse().getStatus()
                    );
                });
    }
}