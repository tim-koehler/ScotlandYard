package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIoJsonImpl

import java.io._

import de.htwg.se.scotlandyard.model.core.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import play.api.libs.json._
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.{Detective, MrX}

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load(): Unit = {
    val source: String = Source.fromFile("ScotlandYard.json").getLines.mkString
    val json = Json.parse(source)

    GameMaster.round = (json \ "round").get.toString().toInt
    GameMaster.totalRound = (json \ "totalRound").get.toString().toInt
    val name = (json \ "mrX" \ "name").get.toString()
    val stationNumber = (json \ "mrX" \ "stationNumber").get.toString().toInt
    val isVisible = (json \ "mrX" \ "isVisible").get.toString().toBoolean
    val lastSeen = (json \ "mrX" \ "lastSeen").asOpt[String]
    val blackTickets = (json \ "mrX" \ "blackTickets").get.toString().toInt
    val doubleTurns = (json \ "mrX" \ "doubleTurns").get.toString().toInt
    val taxiTickets = (json \ "mrX" \ "taxiTickets").get.toString().toInt
    val busTickets = (json \ "mrX" \ "busTickets").get.toString().toInt
    val undergroundTickets = (json \ "mrX" \ "undergroundTickets").get.toString().toInt
    GameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen.get, blackTickets, doubleTurns, taxiTickets, busTickets, undergroundTickets)

    val detectives: JsArray = (json \ "detectives").as[JsArray]
    for(detective <- detectives.value) {
        println((detective \ "name").get.toString())
    }
  }

  override def save(): Unit = {
    val mrx = Json.obj(
      "name"         ->  GameMaster.players(0).asInstanceOf[MrX].name,
      "stationNumber"       ->  GameMaster.players(0).asInstanceOf[MrX].station.number.toInt,
      "isVisible"           ->  GameMaster.players(0).asInstanceOf[MrX].isVisible,
      "lastSeen"            ->  GameMaster.players(0).asInstanceOf[MrX].lastSeen,
      "blackTickets"        ->  GameMaster.players(0).asInstanceOf[MrX].blackTickets,
      "doubleTurns"         ->  2,
      "taxiTickets"         ->  3,
      "busTickets"          ->  4,
      "undergroundTickets"  ->  5
    )

    var detectives = new JsArray()
    for(i <- 1 to GameMaster.players.length - 1) {
      detectives = detectives.append(Json.obj(
        "name"         -> GameMaster.players(i).name,
        "stationNumber"       -> GameMaster.players(i).station.number.toInt,
        "taxiTickets"         -> GameMaster.players(i).taxiTickets,
        "busTickets"          -> GameMaster.players(i).busTickets,
        "undergroundTickets"  -> GameMaster.players(i).undergroundTickets
      ))
    }

    val gameStateJson = Json.obj(
      "round" -> GameMaster.round,
      "totalRound"   -> GameMaster.totalRound,
      "nPlayer"      -> GameMaster.players.length,
      "mrX"          -> mrx,
      "detectives"   -> detectives
    )

    val pw = new PrintWriter(new File("ScotlandYard.json"))
    pw.write(Json.prettyPrint(Json.toJson(gameStateJson)))
    pw.close()
  }
}
