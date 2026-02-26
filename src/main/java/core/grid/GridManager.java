package core.grid;

import core.config.ConfigReader;
import java.net.URL;

public class GridManager {

    public static URL getGridUrl() {
        try {
            return new URL(ConfigReader.get("gridUrl"));
        } catch (Exception e) {
            throw new RuntimeException("Invalid grid URL", e);
        }
    }
}