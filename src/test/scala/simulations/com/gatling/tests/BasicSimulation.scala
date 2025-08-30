package simulations.com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  // Define HTTP Protocol
  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io") // Demo site
    .acceptHeader("application/json")
    .userAgentHeader("Gatling Basic Test")

  // Define Scenario
  val scn = scenario("Basic Load Test")
    .exec(
      http("Get Computers List")
        .get("/computers")
        .check(status.is(200))
    )

  // Setup Load
  setUp(
    scn.inject(
      atOnceUsers(5) // Run with 5 users immediately
    )
  ).protocols(httpProtocol)
}
