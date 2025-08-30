package simulations.com.gatling.tests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class UserAndOrderSimulation extends Simulation {

  // ✅ Define HTTP protocol
  val httpProtocol = http
    .baseUrl("https://petstore.swagger.io/v2") // Swagger Petstore example
    .contentTypeHeader("application/json")
    .acceptHeader("application/json")

  // ✅ Scenario 1 - Create a new user using JSON body
  val createUser = scenario("Create New User")
    .exec(
      http("Create User Request")
        .post("/user")
        .body(RawFileBody("bodies/createUser.json")).asJson
        .check(status.is(200))
    )

  // ✅ Scenario 2 - Update an order using XML body
  val updateOrder = scenario("Update Order")
    .exec(
      http("Update Order Request")
        .put("/store/order/5001") // using order id from updateOrder.xml
        .body(RawFileBody("bodies/updateOrder.xml")).asXml
        .check(status.in(200, 201))
    )

  // ✅ Run both scenarios
  setUp(
    createUser.inject(atOnceUsers(2)), // 2 users create request
    updateOrder.inject(atOnceUsers(2)) // 2 users update request
  ).protocols(httpProtocol)
}
