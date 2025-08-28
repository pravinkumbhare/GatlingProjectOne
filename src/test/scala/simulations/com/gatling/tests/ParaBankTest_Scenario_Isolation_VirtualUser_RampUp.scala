package com.gatling.tests

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ParaBankTest_Scenario_Isolation_VirtualUser_RampUp extends Simulation {

  private val httpProtocol = http
    .baseUrl("https://parabank.parasoft.com")

  // Divide into small modules/functions

  val login = exec(
    http("Login with Valid Credential")
      .post("/parabank/login.htm")
      .formParam("username", "pravin.kumbhare")
      .formParam("password", "Yukta_2021")
  )
    .pause(2)

  val fundTransfer = exec(
    http("Fund Transfer Page")
      .get("/parabank/transfer.htm")
  )
    .pause(2)
    .exec(
      http("Transfer Amount")
        .post("/parabank/services_proxy/bank/transfer?fromAccountId=17895&toAccountId=18006&amount=101")
    )
    .pause(2)

  val accountOverview = exec(
    http("Account Overview Page")
      .get("/parabank/overview.htm")
  )
    .pause(2)
    .exec(
      http("Account Activity")
        .get("/parabank/activity.htm?id=17895")
    )
    .pause(2)

  val transactionHistory = exec(
    http("Transaction History")
      .get("/parabank/transaction.htm?id=17895")
  )
    .pause(2)

  val logout = exec(
    http("Logout")
      .get("/parabank/logout.htm")
  )
    .pause(2)

//  // 1st way
//  private val scenario_Isolation_VirtualUser_RampUp =
//    scenario("ParaBankTest").exec(login, fundTransfer, accountOverview, transactionHistory, logout)
//
//  setUp(scenario_Isolation_VirtualUser_RampUp.inject(atOnceUsers(1)))
//    .protocols(httpProtocol)

  // 2nd way
  val customer_1 = scenario("Customer_1").exec(login, accountOverview, logout)
  val customer_2 = scenario("Customer_2").exec(login, fundTransfer, transactionHistory, logout)
  val customer_3 = scenario("Customer_3").exec(login, fundTransfer, accountOverview, logout)

  setUp(
    customer_1.inject(rampUsers(10) during(10)),
    customer_2.inject(rampUsers(6) during(10)),
    customer_3.inject(rampUsers(3) during(5))
  ).protocols(httpProtocol)
    .assertions(
      global.responseTime.mean.lt(500),
      global.successfulRequests.percent.gt(75),
      global.failedRequests.count.is(0)
    )

}


// ==========================================================

//package com.gatling.tests
//
//import scala.concurrent.duration._
//
//import io.gatling.core.Predef._
//import io.gatling.http.Predef._
//
//class MyFirstScript extends Simulation {
//
//  private val httpProtocol = http
//    .baseUrl("https://parabank.parasoft.com/parabank")
//
//  private val scn = scenario("MyFirstScript")
//    .group("Login Flow") {
//      exec(
//        http("Open Login Page")
//          .get("/index.htm")
//      )
//        .pause(1)
//        .exec(
//          http("Submit Credentials")
//            .post("/login.htm")
//            .formParam("username", "pravin.kumbhare")
//            .formParam("password", "Yukta_2021")
//            .check(regex("""/activity\.htm\?id=(\d+)""").find.saveAs("accountId")) // capture 1st accountId
//        )
//    }
//    .pause(1)
//    .group("Account Overview") {
//      exec(
//        http("Account Overview Page")
//          .get("/overview.htm")
//          .check(regex("""/activity\.htm\?id=(\d+)""").findAll.saveAs("allAccountIds")) // capture all account IDs
//      )
//        .exec { session =>
//          // pick 2 accounts for transfer
//          val accounts = session("allAccountIds").as[List[String]]
//          val fromAcc = accounts.headOption.getOrElse("12345")
//          val toAcc   = accounts.lastOption.getOrElse("67890")
//
//          session.set("fromAccountId", fromAcc).set("toAccountId", toAcc)
//        }
//    }
//    .pause(1)
//    .group("Fund Transfer") {
//      exec(
//        http("Transfer Amount")
//          .post("/transfer.htm")
//          .formParam("amount", "100")
//          .formParam("fromAccountId", "${fromAccountId}")
//          .formParam("toAccountId", "${toAccountId}")
//          .check(status.is(200))
//      )
//    }
//    .pause(1)
//    .group("Transaction History") {
//      exec(
//        http("Transaction History")
//          .get("/activity.htm?id=${fromAccountId}")
//          .check(status.is(200))
//      )
//    }
//    .pause(1)
//    .group("Logout Flow") {
//      exec(
//        http("Logout")
//          .get("/logout.htm")
//      )
//    }
//
//  setUp(
//    scn.inject(atOnceUsers(5))
//  ).protocols(httpProtocol)
//    .assertions(
//      global.responseTime.mean.lt(1000),
//      global.successfulRequests.percent.gt(90),
//      global.failedRequests.percent.lt(10) // allow a few failures due to demo site instability
//    )
//}


