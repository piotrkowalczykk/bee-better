package com.kowal.backend.performance;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ArticlePerformanceTest extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // The target URL of your Spring Boot app
            .acceptHeader("application/json")
            .authorizationHeader("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkB0ZXN0LmNvbSIsInJvbGVzIjoiQURNSU4sVVNFUiIsImlhdCI6MTc2OTQ2MDE1OCwiZXhwIjoxNzY5NTQ2NTU4fQ.U1qvNibmkQCpsmQ_BuaNQQigLvw7VRmubQzF9BGTzGcA19G3WWAkgnRERS_hfPYngNH3huPWTHsm18ErK5MhUQ");

    // 2. Scenario Definition (The user's journey)
    ScenarioBuilder scn = scenario("Gym Articles Browsing Scenario")
            .exec(http("Request: Get All Articles")
                    .get("/customer/articles")
                    .check(status().is(200))); // Verify the response is 200 OK

    // 3. Load Simulation Setup
    {
        setUp(
                scn.injectOpen(
                        nothingFor(4),               // 1. Warm-up: Pause for 4 seconds
                        atOnceUsers(10),             // 2. Stress: Inject 10 users simultaneously
                        rampUsers(50).during(30)     // 3. Ramp-up: Gradually add 50 users over 30 seconds
                )
        ).protocols(httpProtocol);
    }
}