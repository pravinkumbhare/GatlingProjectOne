package simulations.com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class FeederDemo extends Simulation{

  // Protocol
  val httpProtocol = http.baseUrl("https://jsonplaceholder.typicode.com")

  // Scenario
  val feeder = csv("data/CSVData.csv").circular

  val feederScenario = scenario("Feeder Demo")
    .repeat(3) {
      feed(feeder)
        .exec { session =>
          println("Name: " + session("Name").as[String])
          println("Address: " + session("Address").as[String])
          println("Country: " + session("Country").as[String])
          session
        }
        .pause(1)
        .exec(
          http("Get Posts")
            .get("/posts/1")
            .check(status.is(200))
        )
    }


  // Setup
  setUp(
    feederScenario.inject(rampUsers(1) during (10))
  ).protocols(httpProtocol)
}
