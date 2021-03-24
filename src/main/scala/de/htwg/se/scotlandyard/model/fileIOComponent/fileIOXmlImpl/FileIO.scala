package de.htwg.se.scotlandyard.model.fileIOComponent.fileIOXmlImpl

import java.awt.Color
import java.io._
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.{GameModel, TicketType, Tickets}
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

import scala.collection.mutable
import scala.swing.Color
import scala.xml._

class FileIO @Inject() (override var gameInitializer: GameInitializerInterface) extends FileIOInterface {

  var pathname = "ScotlandYard.xml"

  override def load(): Boolean = {
    val xmlFile = scala.xml.XML.loadFile(pathname)
    GameModel.round = (xmlFile \\ "game" \ "round").text.toInt
    GameModel.totalRound = (xmlFile \\ "game" \ "totalRound").text.toInt
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
    gameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen, tickets, history)

    val detectives = (xmlFile \\ "game" \ "detectives")

    for(i <- 0 to nPlayer - 2) {
      val name = (detectives \\ "detective" \ "name")(i).text.toString
      val stationNumber = (detectives \\ "detective" \ "stationNumber")(i).text.toInt
      val taxiTickets = (detectives \\ "detective" \ "taxiTickets")(i).text.toInt
      val busTickets = (detectives \\ "detective" \ "busTickets")(i).text.toInt
      val undergroundTickets = (detectives \\ "detective" \ "undergroundTickets")(i).text.toInt
      val color = (detectives \\ "detective" \ "color")(i).text.toString
      gameInitializer.initDetectivesFromLoad(name, stationNumber, Tickets(taxiTickets, busTickets, undergroundTickets), Color.decode(color))
    }
    true
  }

  override def save(): Boolean = {
    val pw = new PrintWriter(new File(pathname))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gametoXML())
    pw.write(xml)
    pw.close

    true
  }

  def gametoXML(): Elem = {
    <game>
      <round>{GameModel.round}</round>
      <totalRound>{GameModel.totalRound}</totalRound>
      <nPlayer>{GameModel.players.length}</nPlayer>
      {mrXtoXml()}
      {allDetectivesToXml()}
    </game>
  }

  def mrXHistoryToXml(): Elem = {
    var xmlString: String = ""
    if (GameModel.getMrX.history.isEmpty) {
      xmlString = "<transport>empty</transport>"
    } else {
      for (h <- GameModel.getMrX.history) {
        xmlString = xmlString + "<transport>" + h + "</transport>"
      }
    }
    xmlString = "<history>" + xmlString + "</history>"
    XML.loadString(xmlString)
  }

  def mrXtoXml(): Elem = {
    <mrX>
      <name>{GameModel.getMrX.name}</name>
      <stationNumber>{GameModel.getMrX.station.number}</stationNumber>
      <isVisible>{GameModel.getMrX.isVisible}</isVisible>
      <lastSeen>{GameModel.getMrX.lastSeen}</lastSeen>
      <blackTickets>{GameModel.getMrX.tickets.blackTickets}</blackTickets>
      <taxiTickets>{GameModel.getMrX.tickets.taxiTickets}</taxiTickets>
      <busTickets>{GameModel.getMrX.tickets.busTickets}</busTickets>
      <undergroundTickets>{GameModel.getMrX.tickets.undergroundTickets}</undergroundTickets>
      {mrXHistoryToXml()}
    </mrX>
  }

  def allDetectivesToXml(): Elem = {
    var detectiveString = ""
    for(i <- 1 to GameModel.players.length - 1) {
      detectiveString = detectiveString + detectiveToXmlString(GameModel.players(i).name, GameModel.players(i).station.number, GameModel.players(i).tickets, GameModel.players(i).color)
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
