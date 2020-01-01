package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIoJsonImpl

import java.io._

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import play.api.libs.json._
import de.htwg.se.scotlandyard.model.core.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load(): Unit = {
    val source: String = Source.fromFile("ScotlandYard.json").getLines.mkString
    val json: JsValue = Json.parse(source)
    GameMaster.round = (json \ "game" \ "round").get.toString().toInt
    GameMaster.totalRound = (json \ "game" \ "totalRound").get.toString().toInt
    val name = (json \ "game" \ "mrX" \ "name").get.toString()
    val stationNumber = (json \ "game" \ "mrX" \ "stationNumber").get.toString().toInt
    val isVisible = (json \ "game" \ "mrX" \ "isVisible").get.toString().toBoolean
    val lastSeen = (json \ "game" \ "mrX" \ "lastSeen").asOpt[String]
    val blackTickets = (json \ "game" \ "mrX" \ "blackTickets").get.toString().toInt
    val doubleTurns = (json \ "game" \ "mrX" \ "doubleTurns").get.toString().toInt
    val taxiTickets = (json \ "game" \ "mrX" \ "taxiTickets").get.toString().toInt
    val busTickets = (json \ "game" \ "mrX" \ "busTickets").get.toString().toInt
    val undergroundTickets = (json \ "game" \ "mrX" \ "undergroundTickets").get.toString().toInt
    GameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen.get, blackTickets, doubleTurns, taxiTickets, busTickets, undergroundTickets)
  }

  override def save(): Unit = {
    val pw = new PrintWriter(new File("ScotlandYard.json"))
    pw.write(Json.prettyPrint(gameMasterToJson()))
    pw.close
  }

  def gameMasterToJson() = {
    Json.obj(
      "game" -> Json.obj(
        "round" -> JsNumber(GameMaster.round),
        "totalRound" -> JsNumber(GameMaster.totalRound),
        "nPlayer" -> JsNumber(GameMaster.players.length),
        "mrX" -> Json.obj(
          "name" -> JsString(GameMaster.players(0).asInstanceOf[MrX].name),
          "stationNumber" -> JsNumber(GameMaster.players(0).asInstanceOf[MrX].mrXstation.number.toInt),
          "isVisible" -> JsBoolean(GameMaster.players(0).asInstanceOf[MrX].isVisible),
          "lastSeen" -> JsString(GameMaster.players(0).asInstanceOf[MrX].lastSeen),
          "blackTickets" -> JsNumber(GameMaster.players(0).asInstanceOf[MrX].blackTickets),
          "doubleTurns" -> JsNumber(GameMaster.players(0).asInstanceOf[MrX].doubleTurn),
          "taxiTickets" -> JsNumber(GameMaster.players(0).taxiTickets),
          "busTickets" -> JsNumber(GameMaster.players(0).busTickets),
          "undergroundTickets" -> JsNumber(GameMaster.players(0).undergroundTickets)
          //TODO: MrX History
        ),
        "detectives" -> Json.toJson(
          for(i <- 1 to GameMaster.players.length - 1) yield {
            Json.obj(
            "name" -> JsString(GameMaster.players(i).name),
            "stationNumber" -> JsNumber(GameMaster.players(i).station.number.toInt),
            "taxiTickets" -> JsNumber(GameMaster.players(i).taxiTickets),
            "busTickets" -> JsNumber(GameMaster.players(i).busTickets),
            "undergroundTickets" -> JsNumber(GameMaster.players(i).undergroundTickets)
            )
          }
        )
      )
    )
  }

}
