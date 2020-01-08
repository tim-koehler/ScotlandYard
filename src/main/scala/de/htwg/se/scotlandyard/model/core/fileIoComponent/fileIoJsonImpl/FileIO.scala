package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIoJsonImpl

import java.io._

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import play.api.libs.json._
import de.htwg.se.scotlandyard.model.core.fileIoComponent.{DetectiveSmall, FileIOInterface, GameStats, MrXSmall}
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.{Detective, MrX}

import scala.io.Source

class FileIO extends FileIOInterface {

  override def load(): Unit = {
    val source: String = Source.fromFile("ScotlandYard.json").getLines.mkString
    val json: JsValue = Json.parse(source)
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

    /*
    implicit val detectiveSmallRead = new Reads[DetectiveSmall] {
      override def reads(json: JsValue): JsResult[DetectiveSmall] = for {
        name <- (json \ "name").validate[String]
        stationNumber <- (json \ "stationNumber").validate[Int]
        taxiTickets = (json \ "taxiTickets").validate[Int]
        busTickets = (json \ "busTickets").validate[Int]
        undergroundTickets = (json \ "undergroundTickets").validate[Int]
      } yield DetectiveSmall(name, stationNumber, taxiTickets, busTickets, undergroundTickets)
    }

    val detectives = (json \\ "detectives").toList

    println(detectives(0).as[DetectiveSmall].name)

     */

  }


  override def save(): Unit = {
    val players = GameMaster.players
    val mrx = GameMaster.players(0).asInstanceOf[MrX]

    var detectives = List[DetectiveSmall]()
    for(i <- 1 to players.length - 1) {
      detectives = detectives ::: List(DetectiveSmall(players(i).name, players(i).station.number, players(i).taxiTickets, players(i).busTickets, players(i).undergroundTickets))
    }

    val mrxSmall = MrXSmall(mrx.name, mrx.station.number, mrx.isVisible, mrx.lastSeen, mrx.blackTickets, mrx.doubleTurn, mrx.taxiTickets, mrx.busTickets, mrx.undergroundTickets)
    val gs = GameStats(GameMaster.round, GameMaster.totalRound, GameMaster.players.length, mrxSmall, detectives)


    implicit val gameStatsWrites = new Writes[GameStats] {
      def writes(gs: GameStats) = Json.obj(
        "round" -> gs.round,
        "totalRound" -> gs.totalRound,
        "nPlayer" -> gs.nPlayer,
        "mrX" -> Json.obj(
          "name" -> gs.mrX.name,
          "stationNumber" -> gs.mrX.stationNumber,
          "isVisible" -> gs.mrX.isVisible,
          "lastSeen" -> gs.mrX.lastSeen,
          "blackTickets" -> gs.mrX.blackTickets,
          "doubleTurns" -> gs.mrX.doubleTurns,
          "taxiTickets" -> gs.mrX.taxiTickets,
          "busTickets" -> gs.mrX.busTickets,
          "undergroundTickets" -> gs.mrX.undergroundTickets
          //TODO: MrX History
        ),
        "detectives" -> Json.toJson(
          for(i <- 0 to gs.detectives.length - 1) yield {
            Json.obj(
              "name" -> gs.detectives(i).name,
              "stationNumber" -> gs.detectives(i).stationNumber,
              "taxiTickets" -> gs.detectives(i).taxiTickets,
              "busTickets" -> gs.detectives(i).busTickets,
              "undergroundTickets" -> gs.detectives(i).undergroundTickets
            )
          }
        )
      )
    }

    val json = Json.toJson(gs)

    val pw = new PrintWriter(new File("ScotlandYard.json"))
    pw.write(Json.prettyPrint(json))
    pw.close
  }

}
