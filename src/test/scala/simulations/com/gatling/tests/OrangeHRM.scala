package simulations.com.gatling.tests

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class OrangeHRM extends Simulation {

  val httpProtocol = http
    .baseUrl("https://opensource-demo.orangehrmlive.com")
    .inferHtmlResources()

  // Stubbed headers (replace with real ones if needed)
  val headers_0  = Map("Accept" -> "text/html")
  val headers_4  = Map("Accept" -> "application/json")
  val headers_5  = Map("Accept" -> "application/json")
  val headers_7  = Map("Accept" -> "application/json")
  val headers_15 = Map("Accept" -> "application/json")
  val headers_16 = Map("Accept" -> "application/json")
  val headers_17 = Map("Accept" -> "application/json")
  val headers_22 = Map("Accept" -> "application/json")
  val headers_24 = Map("Accept" -> "application/json")
  val headers_26 = Map("Accept" -> "application/json")

  val scn = scenario("OrangeHRM")
    .exec(
      http("request_0")
        .get("/")
        .headers(headers_0)
    )
    .pause(5)
    .exec(
      http("request_1")
        .get("/web/index.php/auth/login")
        .headers(headers_4)
    )

  setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
}
