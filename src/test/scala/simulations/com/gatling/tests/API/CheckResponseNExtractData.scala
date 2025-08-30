package simulations.com.gatling.tests.API

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class CheckResponseNExtractData extends Simulation{

  // Protocol
  val httpProtocol = http.baseUrl("https://petstore.swagger.io/v2")


  // Scenario
  val createNewUser = scenario("Check correlation with Create + Get User")
    // Step 1: Create a new user
    .exec(
      http("Create User")
        .post("/user")
        .body(StringBody(
          """{
            |  "id": 12345,
            |  "username": "pravin.shah",
            |  "firstName": "pravin",
            |  "lastName": "shah",
            |  "email": "pravin.shah@gmail.com",
            |  "password": "12345",
            |  "phone": "9988776655",
            |  "userStatus": 0
            |}
            |""".stripMargin
        )).asJson
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("userId"))
        .check(bodyString.saveAs("createResponse"))
    )
    .exec { session =>
      println("=========== Create User Response ================")
      println(session("createResponse").as[String])
      println("===================================")
      session
    }
    .pause(2)

    // Step 2: Get that same user
    .exec {
      http("get specific User")
        .get("/user/${username}")
        .check(status.in(200, 204))
        .check(bodyString.saveAs("getUserResponse"))
    }
    .pause(2)

    .exec { session =>

      println("===========Response Body for Specific User ================")
      println(session("getUserResponse").as[String])   // print to console
      println("===================================")

      session
    }

  // setUp
  setUp(createNewUser.inject(rampUsers(1) during(10 seconds)))
    .protocols(httpProtocol)


  // Protocol
//  val httpProtocol = http.baseUrl("https://gorest.co.in/")
//    .header("Authorization","Bearer 66d43ebf924ed687de3c4952054ed9eb8abd590fdadf00b074b9b0713d3f2178")

//  // Scenario
//  val createNewUser = scenario("Check correlation with Create + Get User")
//    // Step 1: Create a new user
//    .exec(
//      http("Create User")
//        .post("public/v2/users")
//        .body(StringBody(
//          """{
//            "name": "Raj Rao",
//            "gender": "male",
//            "email": "raj@gmail.com",
//            "status": "active"
//          }"""
//        )).asJson
//        .check(status.is(201))
//        .check(jsonPath("$.id").saveAs("userId"))
//        .check(bodyString.saveAs("createResponse"))
//    )
//    .exec { session =>
//      println("=========== Create User Response ================")
//      println(session("createResponse").as[String])
//      println("===================================")
//      session
//    }
//    .pause(2)
//
//    // Step 2: Get that same user
//    .exec {
//      http("get specific User")
//        .get("public/v2/users/${userId}")
//        .check(status.in(200, 204))
//        .check(bodyString.saveAs("responseBody2"))
//    }
//    .pause(2)
//
//    .exec { session1 =>
//
//      println("===========Response Body for Specific User ================")
//      println(session1("responseBody2").as[String])   // print to console
//      println("===================================")
//
//      session1
//    }
//
//  // setUp
//  setUp(createNewUser.inject(rampUsers(1) during(10 seconds)))
//    .protocols(httpProtocol)
}
