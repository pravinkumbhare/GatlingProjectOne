package simulations.com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class AdvancedSimulation extends Simulation {

  // HTTP Protocol
  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io") // Demo site
    .acceptHeader("application/json")
    .userAgentHeader("Gatling Advanced Test")

  // CSV Feeder for search
  val feeder = csv("data/search.csv").circular

  // Scenario with multiple steps
  val scn = scenario("Advanced Load Test")
    .feed(feeder)
    .exec(
      http("Home Page")
        .get("/computers")
        .check(status.is(200))
    )
    .pause(2)
    .exec(
      http("Search Computer")
        .get("/computers?f=${searchKey}")
        .check(status.is(200))
    )
    .pause(1)
    .exec(
      http("View Computer Details")
        .get("/computers/355") // Example static ID
        .check(status.is(200))
    )

  // Load Setup
  setUp(
    scn.inject(
      nothingFor(5.seconds),             // wait before starting
      atOnceUsers(10),                   // 10 users immediately
      rampUsers(50) during (30.seconds), // ramp to 50 users over 30s
      constantUsersPerSec(20) during (1.minute) // sustain load
    )
  ).protocols(httpProtocol)
}
