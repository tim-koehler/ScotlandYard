package de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl

import java.awt.Color
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point
import spray.json._
import de.htwg.se.scotlandyard.model.JsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

class GameInitializer() extends GameInitializerInterface {
  val stationsJsonFilePath = "./resources/stations.json"
  val MRX_COLOR: Color = Color.BLACK
  val DT1_COLOR: Color = Color.BLUE
  val DT2_COLOR: Color = Color.decode("#1c8c1c")
  val DT3_COLOR: Color = Color.decode("#de991b")
  val DT4_COLOR: Color = Color.MAGENTA
  val DT5_COLOR: Color = Color.RED
  val DT6_COLOR: Color = Color.decode("#2a9fcc")

  val colorList = Vector(MRX_COLOR, DT1_COLOR, DT2_COLOR, DT3_COLOR, DT4_COLOR, DT5_COLOR, DT6_COLOR)

  // real starting positions
  val detectiveStartPositions = Vector(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174) // 16
  val misterXStartPositions = Vector(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172) // 13
  // drawn Detective Positions
  var drawnPositions: Vector[Int] = Vector()
  val numberOfTaxiTickets = 11
  val numberOfBusTickets = 8
  val numberOfUndergroundTickets = 4

  //TODO: error when list size is 1 or less
  override var MAX_DETECTIVE_LIST_INDEX: Int = detectiveStartPositions.length - 1
  override var MAX_MISTERX_LIST_INDEX: Int = misterXStartPositions.length - 1

  override def initialize(nPlayers: Int = 3, stationsSource: String): GameModel = {
    val stations = initStations(stationsSource)
    GameModel(stations = stations, players = initPlayers(nPlayers, stations))
  }
  
  def getColorList(): Vector[Color] = {
    this.colorList
  }

  private def initStations(stationsSource: String): Vector[Station] = {
    val stations = stationsSource.parseJson.convertTo[Vector[Station]]
    stations.sortWith((s: Station, t: Station) => s.number < t.number)
  }

  private def initPlayers(nPlayer: Int, stations: Vector[Station]): Vector[Player] = {
    /*
    * Commented out, to get always the same starting positions for performance testing
    *
    val mrX = MrX(history = List(), station = stations(drawMisterXPosition()), tickets = Tickets(99, 99, 99, 5))
    var players = List[Player](mrX)
    for(i <- 1 until nPlayer) {
      val detective = Detective(station = stations(drawDetectivePosition()), name = "Dt" + i, color = colorList(i), false, Tickets(numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets))
      players = players ::: List(detective)
    }
    players.toVector
     */

    val mrX = MrX(history = List(), station = stations(35), tickets = Tickets(99, 99, 99, 5))
    var players = List[Player](mrX)
    for(i <- 1 until nPlayer) {
      val detective = Detective(station = stations(detectiveStartPositions(i - 1)), name = "Dt" + i, color = colorList(i), false, Tickets(numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets))
      players = players ::: List(detective)
    }
    players.toVector
  }

  private def drawMisterXPosition(): Int = {
    val startPosIndex = r.nextInt(MAX_MISTERX_LIST_INDEX)
    misterXStartPositions(startPosIndex)
  }

  private def drawDetectivePosition(): Int = {
    var startPosIndex = 0
    do {
      startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    }
    while (drawnPositions.contains(startPosIndex))
      drawnPositions = drawnPositions :+ startPosIndex
    detectiveStartPositions(startPosIndex)
  }
}
