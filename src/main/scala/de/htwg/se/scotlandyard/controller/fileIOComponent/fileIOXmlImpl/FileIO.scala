package de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOXmlImpl

import java.awt.Color
import java.io._
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

import scala.::
import scala.collection.mutable
import scala.swing.Color
import scala.xml._

class FileIO @Inject() (override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.xml"

  override def load(): GameModel = {
    val gameModel = gameInitializer.initialize(3)

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

    var history: mutable.Stack[TicketType] = mutable.Stack()
    val his = (xmlFile \\ "game" \ "mrX" \ "history")

    if(!(his \\ "transport")(0).text.toString.equals("empty")) {
      for(i <- 0 to (his \\ "transport").length - 1) {
        val s: String = (his \\ "transport")(i).text.toString
        history.push(TicketType.withName(s))
      }
    }

    val tickets = Tickets(taxiTickets, busTickets, undergroundTickets, blackTickets)
    val mrx = gameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen, tickets, history, gameModel.stations)

    val detectivesXML = (xmlFile \\ "game" \ "detectives")
    var detectives: List[DetectiveInterface] = List()

    for(i <- 0 to nPlayer - 2) {
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

  override def save(gameModel: GameModel): Boolean = {
    val pw = new PrintWriter(new File(pathname))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gametoXML(gameModel))
    pw.write(xml)
    pw.close

    true
  }

  def gametoXML(gameModel: GameModel): Elem = {
    <game>
      <round>{gameModel.round}</round>
      <totalRound>{gameModel.totalRound}</totalRound>
      <nPlayer>{gameModel.players.length}</nPlayer>
      {mrXtoXml(gameModel)}
      {allDetectivesToXml(gameModel)}
    </game>
  }

  def mrXHistoryToXml(gameModel: GameModel): Elem = {
    var xmlString: String = ""
    if (gameModel.getMrX.history.isEmpty) {
      xmlString = "<transport>empty</transport>"
    } else {
      for (h <- gameModel.getMrX.history) {
        xmlString = xmlString + "<transport>" + h + "</transport>"
      }
    }
    xmlString = "<history>" + xmlString + "</history>"
    XML.loadString(xmlString)
  }

  def mrXtoXml(gameModel: GameModel): Elem = {
    <mrX>
      <name>{gameModel.getMrX.name}</name>
      <stationNumber>{gameModel.getMrX.station.number}</stationNumber>
      <isVisible>{gameModel.getMrX.isVisible}</isVisible>
      <lastSeen>{gameModel.getMrX.lastSeen}</lastSeen>
      <blackTickets>{gameModel.getMrX.tickets.blackTickets}</blackTickets>
      <taxiTickets>{gameModel.getMrX.tickets.taxiTickets}</taxiTickets>
      <busTickets>{gameModel.getMrX.tickets.busTickets}</busTickets>
      <undergroundTickets>{gameModel.getMrX.tickets.undergroundTickets}</undergroundTickets>
      {mrXHistoryToXml(gameModel)}
    </mrX>
  }

  def allDetectivesToXml(gameModel: GameModel): Elem = {
    var detectiveString = ""
    for(i <- 1 to gameModel.players.length - 1) {
      detectiveString = detectiveString + detectiveToXmlString(gameModel.players(i).name, gameModel.players(i).station.number, gameModel.players(i).tickets, gameModel.players(i).color)
    }
    detectiveString = "<detectives>" + detectiveString + "</detectives>"
    XML.loadString(detectiveString)
  }

  def detectiveToXmlString(name: String, stationNumber: Int, tickets: Tickets, color: Color): String = {
    "<detective><name>" + name + "</name> " +
      "<stationNumber>" + stationNumber + "</stationNumber>" +
      "<taxiTickets>" + tickets.taxiTickets + "</taxiTickets>" +
      "<busTickets>" + tickets.busTickets + "</busTickets>" +
      "<undergroundTickets>" + tickets.undergroundTickets + "</undergroundTickets>" +
      "<color>" + String.valueOf(color.getRGB) + "</color></detective>"
  }
}
