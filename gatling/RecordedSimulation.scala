
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val delay = 1

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
	  .disableCaching

	val scn = scenario("RecordedSimulation").repeat(10) {
		exec(http("init_5_player")
			.post("/initialize?numberOfPlayer=5"))
		.pause(delay)
		.exec(http("set_name_1")
			.post("/setPlayerName?newName=Babo1&index=1"))
		.pause(delay)
		.exec(http("set_name_2")
			.post("/setPlayerName?newName=Babo2&index=2"))
		.pause(delay)
		.exec(http("set_name_3")
			.post("/setPlayerName?newName=Babo3&index=3"))
		.pause(delay)
		.exec(http("set_name_4")
			.post("/setPlayerName?newName=Babo4&index=4"))
		.pause(delay)
		.exec(http("set_color_1")
			.post("/setPlayerColor?newColor=%231c8c1c&index=1"))
		.pause(delay)
		.exec(http("set_color_2")
			.post("/setPlayerColor?newColor=%235bacac&index=2"))
		.pause(delay)
		.exec(http("set_color_3")
			.post("/setPlayerColor?newColor=%23318cfc&index=3"))
		.pause(delay)
		.exec(http("set_color_4")
			.post("/setPlayerColor?newColor=%23fa1c11&index=4"))
		.pause(delay)
		.exec(http("start_game")
			.get("/startGame"))
		.pause(delay)
		.exec(http("move_mrx_65_taxi")
			.post("/move?newPosition=65&ticketType=Taxi"))
		.pause(delay)
		.exec(http("move_dt1_67_underground")
			.post("/move?newPosition=67&ticketType=Underground"))
		.pause(delay)
		.exec(http("move_dt2_27_taxi")
			.post("/move?newPosition=27&ticketType=Taxi"))
		.pause(delay)
		.exec(http("move_dt3_55_bus")
			.post("/move?newPosition=55&ticketType=Bus"))
		.pause(delay)
		.exec(http("move_dt4_48_taxi")
			.post("/move?newPosition=48&ticketType=Taxi"))
		.pause(delay * 2)
		.exec(http("save")
			.post("/save"))
		.pause(delay * 2)
		.exec(http("load")
			.post("/load"))
	}
	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}