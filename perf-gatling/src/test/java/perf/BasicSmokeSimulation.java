package perf;

import core.config.ConfigReader;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Example Gatling performance test that reuses the framework's base.url.
 *
 * Run with:
 *   mvn -pl perf-gatling gatling:test
 */
public class BasicSmokeSimulation extends Simulation {

    private static final String BASE_URL =
            System.getProperty("perf.base.url",
                    ConfigReader.getOrDefault("base.url", "https://www.google.com"));

    HttpProtocolBuilder httpProtocol = http
            .baseUrl(BASE_URL)
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .userAgentHeader("perf-gatling-sim");

    ScenarioBuilder smokeScenario = scenario("Basic HTTP smoke")
            .exec(
                    http("GET /")
                            .get("/")
                            .check(status().is(200))
            );

    {
        setUp(
                smokeScenario.injectOpen(
                        rampUsersPerSec(1).to(10).during(30),
                        constantUsersPerSec(10).during(60)
                )
        ).protocols(httpProtocol);
    }
}

