package de.htwg.se.scotlandyard.fileio.fileIOJsonImpl

import java.awt.Color
import java.io._
import com.google.inject.Inject
import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.fileio.FileIOInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{MrX, Player}
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import play.api.libs.json._

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileIO @Inject()(override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.json"

  override def load(): GameModel = {
    val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val gameModel = gameInitializer.initialize(3, stationsSource)

    val source: String = Source.fromFile(pathname).getLines.mkString
    val json = Json.parse(source)

    val round = (json \ "round").get.toString().toInt
    val totalRound = (json \ "totalRound").get.toString().toInt
    val name = (json \ "mrX" \ "name").get.toString()
    val stationNumber = (json \ "mrX" \ "stationNumber").get.toString().toInt
    val isVisible = (json \ "mrX" \ "isVisible").get.toString().toBoolean
    val lastSeen = (json \ "mrX" \ "lastSeen").asOpt[String]
    val blackTickets = (json \ "mrX" \ "blackTickets").get.toString().toInt
    val taxiTickets = (json \ "mrX" \ "taxiTickets").get.toString().toInt
    val busTickets = (json \ "mrX" \ "busTickets").get.toString().toInt
    val undergroundTickets = (json \ "mrX" \ "undergroundTickets").get.toString().toInt
    val historyJs: JsArray = (json \ "mrX" \ "history").as[JsArray]
    var history: List[TicketType] = List()
    historyJs.value.foreach(transport => history = TicketType.withName(formatString((transport \ "transport").get.toString())) :: history)

    val tickets = Tickets(taxiTickets, busTickets, undergroundTickets, blackTickets)
    val mrx = gameInitializer.initMrXFromLoad(formatString(name), stationNumber, isVisible, lastSeen.get, tickets, history, gameModel.stations)

    val detectivesJson: JsArray = (json \ "detectives").as[JsArray]
    var detectives: Vector[Player] = Vector()

    for (detective <- detectivesJson.value) {
      val name = (detective \ "name").get.toString()
      val stationNumber = (detective \ "stationNumber").get.toString().toInt
      val taxiTickets = (detective \ "taxiTickets").get.toString().toInt
      val busTickets = (detective \ "busTickets").get.toString().toInt
      val undergroundTickets = (detective \ "undergroundTickets").get.toString().toInt
      val color = (detective \ "color").get.toString()
      detectives = detectives :+ gameInitializer.initDetectiveFromLoad(formatString(name), stationNumber, Tickets(taxiTickets, busTickets, undergroundTickets), Color.decode(formatString(color)), gameModel.stations)
    }

    val players = mrx +: detectives
    gameModel.copy(players = players, round = round, totalRound = totalRound)
  }

  override def save(gameModel: GameModel, mrX: MrX): Boolean = {
    var history = new JsArray()

    for (h <- mrX.history) {
      history = history.append(Json.obj(
        "transport" -> h
      ))
    }
    val mrx = Json.obj(
      "name" -> mrX.name,
      "stationNumber" -> mrX.station.number.toInt,
      "isVisible" -> mrX.isVisible,
      "lastSeen" -> mrX.lastSeen,
      "blackTickets" -> mrX.tickets.blackTickets,
      "taxiTickets" -> mrX.tickets.taxiTickets,
      "busTickets" -> mrX.tickets.busTickets,
      "undergroundTickets" -> mrX.tickets.undergroundTickets,
      "history" -> history
    )

    var detectives = new JsArray()
    for (i <- 1 to gameModel.players.length - 1) {
      detectives = detectives.append(Json.obj(
        "name" -> gameModel.players(i).name,
        "stationNumber" -> gameModel.players(i).station.number.toInt,
        "taxiTickets" -> gameModel.players(i).tickets.taxiTickets,
        "busTickets" -> gameModel.players(i).tickets.busTickets,
        "undergroundTickets" -> gameModel.players(i).tickets.undergroundTickets,
        "color" -> String.valueOf(gameModel.players(i).color.getRGB)
      ))
    }

    val gameStateJson = Json.obj(
      "round" -> gameModel.round,
      "totalRound" -> gameModel.totalRound,
      "nPlayer" -> gameModel.players.length,
      "mrX" -> mrx,
      "detectives" -> detectives
    )

    Try {
      val pw = new PrintWriter(new File(pathname))
      pw.write(Json.prettyPrint(Json.toJson(gameStateJson)))
      pw.close()
    } match
    {
      case Success(v) => return true
      case Failure(e) => return false
    }
  }

  def formatString(s: String): String = {
    s.substring(1, s.length - 1) //removes quotation marks from loaded Strings
  }
}
