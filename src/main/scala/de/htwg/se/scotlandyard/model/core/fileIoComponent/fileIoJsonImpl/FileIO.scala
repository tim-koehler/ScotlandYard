package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIoJsonImpl

import java.awt.Color
import java.io._

import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.core.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.util.{TicketType, Tickets}
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import play.api.libs.json._

import scala.io.Source

class FileIO @Inject() extends FileIOInterface {

  val injector = Guice.createInjector(new ScotlandYardModule)

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
    val historyJs: JsArray = (json \ "mrX" \ "history").as[JsArray]
    var history: List[TicketType] = List()
    for(transport <- historyJs.value) {
      val s = (transport \ "transport").get.toString()
      history = history:::List(TicketType.withName(formatString(s)))
    }

    val gameInitializer = injector.getInstance(classOf[GameInitializerInterface])
    val tickets = Tickets(taxiTickets, busTickets, undergroundTickets, blackTickets, doubleTurns)
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
  }

  override def save(): Unit = {
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
      "doubleTurns"         ->  GameMaster.getMrX().tickets.doubleTurns,
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

    val pw = new PrintWriter(new File("ScotlandYard.json"))
    pw.write(Json.prettyPrint(Json.toJson(gameStateJson)))
    pw.close()
  }

  def formatString(s: String): String = {
    s.substring(1, s.length - 1) //removes quotation marks from loaded Strings
  }
}
