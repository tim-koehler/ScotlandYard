package de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIOXmlImpl

import java.awt.Color
import java.io._

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.core.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.util.TicketType
import de.htwg.se.scotlandyard.util.TicketType.TicketType

import scala.swing.Color
import scala.xml._

class FileIO extends FileIOInterface {

  override def load(): Unit = {
    val xmlFile = scala.xml.XML.loadFile("ScotlandYard.xml")
    GameMaster.round = (xmlFile \\ "game" \ "round").text.toInt
    GameMaster.totalRound = (xmlFile \\ "game" \ "totalRound").text.toInt
    val nPlayer = (xmlFile \\ "game" \ "nPlayer").text.toInt
    val name = (xmlFile \\ "game" \ "mrX" \ "name").text
    val stationNumber = (xmlFile \\ "game" \ "mrX" \ "stationNumber").text.toInt
    val isVisible = (xmlFile \\ "game" \ "mrX" \ "isVisible").text.toBoolean
    val lastSeen = (xmlFile \\ "game" \ "mrX" \ "lastSeen").text
    val blackTickets = (xmlFile \\ "game" \ "mrX" \ "blackTickets").text.toInt
    val doubleTurns = (xmlFile \\ "game" \ "mrX" \ "doubleTurns").text.toInt
    val taxiTickets = (xmlFile \\ "game" \ "mrX" \ "taxiTickets").text.toInt
    val busTickets = (xmlFile \\ "game" \ "mrX" \ "busTickets").text.toInt
    val undergroundTickets = (xmlFile \\ "game" \ "mrX" \ "undergroundTickets").text.toInt
    var history: List[TicketType] = List()
    val his = (xmlFile \\ "game" \ "mrX" \ "history")
    for(i <- 0 to (xmlFile \\ "game" \ "mrX" \ "history").length) {
      val s: String = (his \\ "transport")(i).text.toString
      history = history:::List(TicketType.withName(s))
    }
    GameInitializer.initMrXFromLoad(name, stationNumber, isVisible, lastSeen, blackTickets, doubleTurns, taxiTickets, busTickets, undergroundTickets, history)

    val detectives = (xmlFile \\ "game" \ "detectives")

    for(i <- 0 to nPlayer - 2) {
      val name = (detectives \\ "detective" \ "name")(i).text.toString
      val stationNumber = (detectives \\ "detective" \ "stationNumber")(i).text.toInt
      val taxiTickets = (detectives \\ "detective" \ "taxiTickets")(i).text.toInt
      val busTickets = (detectives \\ "detective" \ "busTickets")(i).text.toInt
      val undergroundTickets = (detectives \\ "detective" \ "undergroundTickets")(i).text.toInt
      val color = (detectives \\ "detective" \ "color")(i).text.toString
      GameInitializer.initDetectiveFromLoad(name, stationNumber, taxiTickets, busTickets, undergroundTickets, Color.decode(color))
    }

  }

  override def save(): Unit = {
    val pw = new PrintWriter(new File("ScotlandYard.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gametoXML())
    pw.write(xml)
    pw.close
  }

  def gametoXML(): Elem = {
    <game>
      <round>{GameMaster.round}</round>
      <totalRound>{GameMaster.totalRound}</totalRound>
      <nPlayer>{GameMaster.players.length}</nPlayer>
      {mrXtoXml()}
      {allDetectivesToXml()}
    </game>
  }

  def mrXHistoryToXml(): Elem = {
    var xmlString: String = ""
    if (GameMaster.getMrX().history.isEmpty) {
      xmlString = "<transport></transport>"
    } else {
      for (h <- GameMaster.getMrX().history) {
        xmlString = xmlString + "<transport>" + h + "</transport>"
      }
    }
    xmlString = "<history>" + xmlString + "</history>"
    println(xmlString)
    XML.loadString(xmlString)
  }

  def mrXtoXml(): Elem = {
    <mrX>
      <name>{GameMaster.getMrX().name}</name>
      <stationNumber>{GameMaster.getMrX().station.number}</stationNumber>
      <isVisible>{GameMaster.getMrX().isVisible}</isVisible>
      <lastSeen>{GameMaster.getMrX().lastSeen}</lastSeen>
      <blackTickets>{GameMaster.getMrX().blackTickets}</blackTickets>
      <doubleTurns>{GameMaster.getMrX().doubleTurn}</doubleTurns>
      <taxiTickets>{GameMaster.getMrX().taxiTickets}</taxiTickets>
      <busTickets>{GameMaster.getMrX().busTickets}</busTickets>
      <undergroundTickets>{GameMaster.getMrX().undergroundTickets}</undergroundTickets>
      {mrXHistoryToXml()}
    </mrX>
  }

  def allDetectivesToXml(): Elem = {
    var detectiveString = ""
    for(i <- 1 to GameMaster.players.length - 1) {
      detectiveString = detectiveString + detectiveToXmlString(GameMaster.players(i).name, GameMaster.players(i).station.number, GameMaster.players(i).taxiTickets, GameMaster.players(i).busTickets, GameMaster.players(i).undergroundTickets, GameMaster.players(i).color)
    }
    detectiveString = "<detectives>" + detectiveString + "</detectives>"
    XML.loadString(detectiveString)
  }

  def detectiveToXmlString(name: String, stationNumber: Int, taxiTickets: Int, busTickets: Int, undergroundTickets: Int, color: Color): String = {
    "<detective><name>" + name + "</name> " +
      "<stationNumber>" + stationNumber + "</stationNumber>" +
      "<taxiTickets>" + taxiTickets + "</taxiTickets>" +
      "<busTickets>" + busTickets + "</busTickets>" +
      "<undergroundTickets>" + undergroundTickets + "</undergroundTickets>" +
      "<color>" + String.valueOf(color.getRGB) + "</color></detective>"
  }

}
