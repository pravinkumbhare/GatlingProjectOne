package com.gatling.tests

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class MyFirstScript extends Simulation {

	private val httpProtocol = http
		.baseUrl("https://opensource-demo.orangehrmlive.com")
	// .disableFollowRedirect   // <- uncomment if you don’t want auto redirects

	private val scn = scenario("MyFirstScript")
		.group("Login Flow") {
			exec(
				http("Open Login Page")
					.get("/web/index.php/auth/login")
			)
				.pause(1)
				.exec(
					http("Submit Credentials")
						.post("/web/index.php/auth/validate")
						.formParam("username", "Admin")
						.formParam("password", "admin123")
				)
		}
		.pause(1)
		.group("Logout Flow") {
			exec(
				http("Logout")
					.get("/web/index.php/auth/logout")
			)
		}

	setUp(
		scn.inject(atOnceUsers(15))
	).protocols(httpProtocol)
		.assertions(
			global.responseTime.mean.lt(1000),
			global.successfulRequests.percent.gt(95),
			global.failedRequests.count.is(0)

//			// Per-request assertions (fix: no spaces)
//			details("Login Flow/Open Login Page").responseTime.max.lt(5000),
//			details("Login Flow/Submit Credentials").successfulRequests.percent.is(100),
//			details("Logout Flow/Logout").responseTime.mean.lt(2000)

	)
}
