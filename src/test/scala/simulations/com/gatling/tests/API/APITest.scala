package simulations.com.gatling.tests.API

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class APITest extends Simulation{

  // Protocol
  val httpProtocol = http.baseUrl("https://reqres.in/api/users")

  // Scenario
  val scn = scenario("GET API Request Demo")
    .exec(
      http("Get Single User")
        .get("/2")
        .check(status.is(200))
        .check(jsonPath("$.data.first_name").is("Janet"))
    )
    .pause(2)

  // SetUp
  setUp(
//    scn.inject(atOnceUsers(10)).protocols(httpProtocol)
    scn.inject(rampUsers(10).during(10)).protocols(httpProtocol)
  )


}
