package simulations.com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoBlazeTest extends Simulation {

  private val httpProtocol = http
    .baseUrl("https://www.demoblaze.com")
    .inferHtmlResources()

  private val uri2 = "https://api.demoblaze.com"

  private val uri3 = "https://hls.demoblaze.com/index.m3u8"

  private val scn = scenario("DemoBlazeTest")

    .exec(
      http("Login Request")
        .options(uri2 + "/login")
    )

  pause(13)

    .exec(
      http("Select Category")
        .options(uri2 + "/bycat")
    )

  pause(6)

    .exec(
      http("Select Item")
        .get("/prod.html?idp_=1")
    )

  pause(4)

    .exec(
      http("Add Item to Category")
        .options(uri2 + "/addtocart")
    )


  pause(6)

    .exec(
      http("Go To Cart Page")
        .get("/cart.html")
    )

  pause(5)

    .exec(
      http("To Delete Category")
        .options(uri2 + "/deletecart")
    )

  pause(5)

    .exec(
      http("request_34")
        .get("/index.html")
    )

    .exec(
      http("request_40")
        .get("/index.html")
    )
  pause(5)

  setUp(scn.inject(atOnceUsers(5))).protocols(httpProtocol)
}
