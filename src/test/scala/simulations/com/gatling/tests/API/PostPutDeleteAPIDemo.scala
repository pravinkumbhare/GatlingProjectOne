package simulations.com.gatling.tests.API

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class PostPutDeleteAPIDemo extends Simulation {

  // Protocol
  val httpProtocol = http.baseUrl("https://jsonplaceholder.typicode.com")

  // Scenario
  val createScenario = scenario("Post Put Delete API Request Demo")
    // POST
    .exec(
      http("Create Post")
        .post("/posts")
        .header("Content-Type", "application/json")
        .body(RawFileBody("data/user.json")).asJson
//        .body(StringBody(
//          """
//          {
//            "title": "foo",
//            "body": "bar",
//            "userId": 1
//          }
//        """
//        )).asJson
        .check(status.is(201))
        .check(jsonPath("$.title").is("foo"))
        .check(jsonPath("$.id").saveAs("postId"))
    )
    .pause(1)

    // PUT (Update)
    val updateScenario = scenario("Update User")
    .exec(
      http("Update created POST")
//        .put("/posts/${postId}")
        .put("/posts/1")
        .header("Content-Type", "application/json")
        .body(RawFileBody("data/user.json")).asJson
//        .body(StringBody(
//          """
//          {
//            "title": "updated title",
//            "body": "updated body",
//            "userId": 1
//          }
//        """
//        )).asJson
        .check(status.is(200))
    )
    .pause(1)

    // DELETE
    val deleteScenario = scenario("Delete User")
    .exec(
      http("Delete Post")
        .delete("/posts/${postId}")
        .check(status.is(200))
    )


  // Setup
  setUp(
    createScenario.inject(rampUsers(3)during(3)),
    updateScenario.inject(rampUsers(3).during(3)),
    deleteScenario.inject(rampUsers(3).during(3))

  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.mean.lt(2000),              // ✅ mean response < 2s
      global.successfulRequests.percent.gt(95)        // ✅ >95% success
    )
}
