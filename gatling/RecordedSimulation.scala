
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("PostmanRuntime/7.28.0")

	val headers_0 = Map("Postman-Token" -> "8278fa84-8ab7-4a0a-8b1f-a646c8cc04b5")

	val headers_1 = Map("Postman-Token" -> "8471ba16-42a2-405a-8a35-6a87b20975ea")

	val headers_2 = Map("Postman-Token" -> "aa4e4a70-247c-42f7-ac65-bc81e3521011")

	val headers_3 = Map("Postman-Token" -> "1faa0c6b-9b13-4d14-b8ec-98520dcbe194")

	val headers_4 = Map("Postman-Token" -> "6438c372-aed8-42cc-a8ac-75c9ac71a8ba")

	val headers_5 = Map("Postman-Token" -> "37903e6b-0d0b-4f0d-88a6-c2d220296568")

	val headers_6 = Map("Postman-Token" -> "a657e262-c15e-46c4-ab6f-00bd2b027a90")

	val headers_7 = Map("Postman-Token" -> "417f02ee-57ab-40f6-a153-db38557ad465")

	val headers_8 = Map("Postman-Token" -> "8579bf3b-815b-4f65-8970-c366082a0c5c")

	val headers_9 = Map("Postman-Token" -> "88162836-4c57-4ff8-b511-4b9dfea7b5df")

	val headers_10 = Map("Postman-Token" -> "4db9a1b6-ca4a-4e96-80b7-7695b92b736e")

	val headers_11 = Map("Postman-Token" -> "80897588-3585-4263-a775-8419cfaba509")

	val headers_12 = Map("Postman-Token" -> "96e8fcb6-58c9-4c03-8e51-d83b91bc3a6a")



	val scn = scenario("RecordedSimulation")
		.exec(http("init_5_player")
			.post("/initialize?numberOfPlayer=5")
			.headers(headers_0))
		.pause(2)
		.exec(http("set_name_1")
			.post("/setPlayerName?newName=Babo1&index=1")
			.headers(headers_1))
		.pause(2)
		.exec(http("set_name_2")
			.post("/setPlayerName?newName=Babo2&index=2")
			.headers(headers_2))
		.pause(2)
		.exec(http("set_name_3")
			.post("/setPlayerName?newName=Babo3&index=3")
			.headers(headers_3))
		.pause(2)
		.exec(http("set_name_4")
			.post("/setPlayerName?newName=Babo4&index=4")
			.headers(headers_4))
		.pause(2)
		.exec(http("set_color_1")
			.post("/setPlayerColor?newColor=cc00cff&index=1")
			.headers(headers_5))
		.pause(2)
		.exec(http("set_color_2")
			.post("/setPlayerColor?newColor=cc02fc&index=2")
			.headers(headers_6))
		.pause(2)
		.exec(http("set_color_3")
			.post("/setPlayerColor?newColor=cc03cc&index=3")
			.headers(headers_7))
		.pause(2)
		.exec(http("set_color_4")
			.post("/setPlayerColor?newColor=cc04cc&index=4")
			.headers(headers_8))
		.pause(2)
		.exec(http("start_game")
			.get("/startGame")
			.headers(headers_9))
		.pause(2)
		.exec(http("move_mrx_65_taxi")
			.post("/move?newPosition=65&ticketType=Taxi")
			.headers(headers_10))
		.pause(2)
		.exec(http("move_dt1_67_underground")
			.post("/move?newPosition=67&ticketType=Underground")
			.headers(headers_11))
		.pause(2)
		.exec(http("move_dt2_27_taxi")
			.post("/move?newPosition=27&ticketType=Taxi")
			.headers(headers_12))
		.pause(2)
		.exec(http("move_dt3_55_bus")
			.post("/move?newPosition=55&ticketType=Bus")
			.headers(headers_12))
		.pause(2)
		.exec(http("move_dt4_48_taxi")
			.post("/move?newPosition=48&ticketType=Taxi")
			.headers(headers_12))
		.pause(3)
		.exec(http("save")
			.post("/save")
			.headers(headers_12))
		.pause(3)
		.exec(http("load")
			.post("/load")
			.headers(headers_12))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}