package de.htwg.se.scotlandyard.model.fileIOComponent.fileIOJsonImpl

import java.awt.Color
import java.io._

import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.util.{TicketType, Tickets}
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import play.api.libs.json._

import scala.collection.mutable
import scala.io.Source

class FileIO @Inject()(override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.json"

  override def load(): Boolean = {
    val source: String = Source.fromFile(pathname).getLines.mkString
    val json = Json.parse(source)

    GameMaster.round = (json \ "round").get.toString().toInt
    GameMaster.totalRound = (json \ "totalRound").get.toString().toInt
    val name = (json \ "mrX" \ "name").get.toString()
    val stationNumber = (json \ "mrX" \ "stationNumber").get.toString().toInt
    val isVisible = (json \ "mrX" \ "isVisible").get.toString().toBoolean
    val lastSeen = (json \ "mrX" \ "lastSeen").asOpt[String]
    val blackTickets = (json \ "mrX" \ "blackTickets").get.toString().toInt
    val taxiTickets = (json \ "mrX" \ "taxiTickets").get.toString().toInt
    val busTickets = (json \ "mrX" \ "busTickets").get.toString().toInt
    val undergroundTickets = (json \ "mrX" \ "undergroundTickets").get.toString().toInt
    val historyJs: JsArray = (json \ "mrX" \ "history").as[JsArray]
    var history: mutable.Stack[TicketType] = mutable.Stack()
    for(transport <- historyJs.value) {
      val s = (transport \ "transport").get.toString()
      history.push(TicketType.withName(formatString(s)))
    }

    val tickets = Tickets(taxiTickets, busTickets, undergroundTickets, blackTickets)
    gameInitializer.initMrXFromLoad(formatString(name), stationNumber, isVisible, lastSeen.get, tickets, history)

    val detectives: JsArray = (json \ "detectives").as[JsArray]

    for(detective <- detectives.value) {
      val name = (detective \ "name").get.toString()
      val stationNumber = (detective \ "stationNumber").get.toString().toInt
      val taxiTickets = (detective \ "taxiTickets").get.toString().toInt
      val busTickets = (detective \ "busTickets").get.toString().toInt
      val undergroundTickets = (detective \ "undergroundTickets").get.toString().toInt
      val color = (detective \ "color").get.toString()
      gameInitializer.initDetectivesFromLoad(formatString(name), stationNumber, Tickets(taxiTickets, busTickets, undergroundTickets), Color.decode(formatString(color)))
    }
    true
  }

  override def save(): Boolean = {
    var history = new JsArray()

    for(h <- GameMaster.getMrX().history) {
      history = history.append(Json.obj(
        "transport" -> h
      ))
    }

    val mrx = Json.obj(
      "name"         ->  GameMaster.getMrX().name,
      "stationNumber"       ->  GameMaster.getMrX().station.number.toInt,
      "isVisible"           ->  GameMaster.getMrX().isVisible,
      "lastSeen"            ->  GameMaster.getMrX().lastSeen,
      "blackTickets"        ->  GameMaster.getMrX().tickets.blackTickets,
      "taxiTickets"         ->  GameMaster.getMrX().tickets.taxiTickets,
      "busTickets"          ->  GameMaster.getMrX().tickets.busTickets,
      "undergroundTickets"  ->  GameMaster.getMrX().tickets.undergroundTickets,
      "history"             ->  history
    )

    var detectives = new JsArray()
    for(i <- 1 to GameMaster.players.length - 1) {
      detectives = detectives.append(Json.obj(
        "name"         -> GameMaster.players(i).name,
        "stationNumber"       -> GameMaster.players(i).station.number.toInt,
        "taxiTickets"         -> GameMaster.players(i).tickets.taxiTickets,
        "busTickets"          -> GameMaster.players(i).tickets.busTickets,
        "undergroundTickets"  -> GameMaster.players(i).tickets.undergroundTickets,
        "color"               -> String.valueOf(GameMaster.players(i).color.getRGB)
      ))
    }

    val gameStateJson = Json.obj(
      "round" -> GameMaster.round,
      "totalRound"   -> GameMaster.totalRound,
      "nPlayer"      -> GameMaster.players.length,
      "mrX"          -> mrx,
      "detectives"   -> detectives
    )

    val pw = new PrintWriter(new File(pathname))
    pw.write(Json.prettyPrint(Json.toJson(gameStateJson)))
    pw.close()

    true
  }

  def formatString(s: String): String = {
    s.substring(1, s.length - 1) //removes quotation marks from loaded Strings
  }
}
