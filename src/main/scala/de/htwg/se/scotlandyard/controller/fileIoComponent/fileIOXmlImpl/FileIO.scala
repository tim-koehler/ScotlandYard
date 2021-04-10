package de.htwg.se.scotlandyard.controller.fileIoComponent.fileIOXmlImpl

import java.awt.Color
import java.io._
import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import TicketType.TicketType
import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.model.players.{MrX, Player}

import scala.io.Source
import scala.swing.Color
import scala.util.{Failure, Success, Try}
import scala.xml._

class FileIO @Inject()(override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.xml"

  override def load(): GameModel = {
    val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val gameModel = gameInitializer.initialize(3, stationsSource)

    val xmlFile = scala.xml.XML.loadFile(pathname)
    val round = (xmlFile \\ "game" \ "round").text.toInt
    val totalRound = (xmlFile \\ "game" \ "totalRound").text.toInt
    val nPlayer = (xmlFile \\ "game" \ "nPlayer").text.toInt
    val name = (xmlFile \\ "game" \ "mrX" \ "name").text
    val stationNumber = (xmlFile \\ "game" \ "mrX" \ "stationNumber").text.toInt
    val isVisible = (xmlFile \\ "game" \ "mrX" \ "isVisible").text.toBoolean
    val lastSeen = (xmlFile \\ "game" \ "mrX" \ "lastSeen").text
    val blackTickets = (xmlFile \\ "game" \ "mrX" \ "blackTickets").text.toInt
    val taxiTickets = (xmlFile \\ "game" \ "mrX" \ "taxiTickets").text.toInt
    val busTickets = (xmlFile \\ "game" \ "mrX" \ "busTickets").text.toInt
    val undergroundTickets = (xmlFile \\ "game" \ "mrX" \ "undergroundTickets").text.toInt

    val his = (xmlFile \\ "game" \ "mrX" \ "history")
    var history: List[TicketType] = List()
    if (!(his \\ "transport")(0).text.toString.equals("empty")) {
      for (i <- 0 to (his \\ "transport").length - 1) {
        val s: String = (his \\ "transport")(i).text.toString
        history = TicketType.withName(s) :: history
      }
    }

    val tickets = Tickets(taxiTickets, busTickets, undergroundTickets, blackTickets)
    val mrx = gameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen, tickets, history, gameModel.stations)

    val detectivesXML = (xmlFile \\ "game" \ "detectives")
    var detectives: List[Player] = List()

    for (i <- 0 to nPlayer - 2) {
      val name = (detectivesXML \\ "detective" \ "name")(i).text.toString
      val stationNumber = (detectivesXML \\ "detective" \ "stationNumber")(i).text.toInt
      val taxiTickets = (detectivesXML \\ "detective" \ "taxiTickets")(i).text.toInt
      val busTickets = (detectivesXML \\ "detective" \ "busTickets")(i).text.toInt
      val undergroundTickets = (detectivesXML \\ "detective" \ "undergroundTickets")(i).text.toInt
      val color = (detectivesXML \\ "detective" \ "color")(i).text.toString
      detectives = gameInitializer.initDetectiveFromLoad(name, stationNumber, Tickets(taxiTickets, busTickets, undergroundTickets), Color.decode(color), gameModel.stations) :: detectives
    }

    val players = mrx :: detectives

    gameModel.copy(players = players.toVector, round = round, totalRound = totalRound)
  }

  override def save(gameModel: GameModel, mrX: MrX): Boolean = {

    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gametoXML(gameModel, mrX))

    Try {
      val pw = new PrintWriter(new File(pathname))
      pw.write(xml)
      pw.close()
    } match {
      case Success(v) => true
      case Failure(e) => false
    }
  }

  def gametoXML(gameModel: GameModel, mrX: MrX): Elem = {
    <game>
      <round>{gameModel.round}</round>
      <totalRound>{gameModel.totalRound}</totalRound>
      <nPlayer>{gameModel.players.length}</nPlayer>
      {mrXtoXml(gameModel, mrX)}
      {allDetectivesToXml(gameModel)}
    </game>
  }

  private def mrXHistoryToXml(mrX: MrX): Elem = {
    var xmlString: String = ""
    if (mrX.history.isEmpty) {
      xmlString = "<transport>empty</transport>"
    } else {
      for (h <- mrX.history) {
        xmlString = xmlString + "<transport>" + h + "</transport>"
      }
    }
    xmlString = "<history>" + xmlString + "</history>"
    XML.loadString(xmlString)
  }

  private def mrXtoXml(gameModel: GameModel, mrX: MrX): Elem = {
    <mrX>
      <name>{mrX.name}</name>
      <stationNumber>{mrX.station.number}</stationNumber>
      <isVisible>{mrX.isVisible}</isVisible>
      <lastSeen>{mrX.lastSeen}</lastSeen>
      <blackTickets>{mrX.tickets.blackTickets}</blackTickets>
      <taxiTickets>{mrX.tickets.taxiTickets}</taxiTickets>
      <busTickets>{mrX.tickets.busTickets}</busTickets>
      <undergroundTickets>{mrX.tickets.undergroundTickets}</undergroundTickets>
      {mrXHistoryToXml(mrX)}
    </mrX>
  }

  private def allDetectivesToXml(gameModel: GameModel): Elem = {
    var detectiveString = ""
    for (i <- 1 to gameModel.players.length - 1) {
      detectiveString = detectiveString + detectiveToXmlString(gameModel.players(i).name, gameModel.players(i).station.number, gameModel.players(i).tickets, gameModel.players(i).color)
    }
    detectiveString = "<detectives>" + detectiveString + "</detectives>"
    XML.loadString(detectiveString)
  }

  private def detectiveToXmlString(name: String, stationNumber: Int, tickets: Tickets, color: Color): String = {
    "<detective><name>" + name + "</name> " +
      "<stationNumber>" + stationNumber + "</stationNumber>" +
      "<taxiTickets>" + tickets.taxiTickets + "</taxiTickets>" +
      "<busTickets>" + tickets.busTickets + "</busTickets>" +
      "<undergroundTickets>" + tickets.undergroundTickets + "</undergroundTickets>" +
      "<color>" + String.valueOf(color.getRGB) + "</color></detective>"
  }
}
