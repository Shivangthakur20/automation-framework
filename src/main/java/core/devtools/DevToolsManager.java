package core.devtools;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v120.network.Network;

import java.util.Optional;

public class DevToolsManager {

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
                    System.out.println(
                            "API: " +
                                    response.getResponse().getUrl() +
                                    " Status: " +
                                    response.getResponse().getStatus()
                    );
                });
    }
}