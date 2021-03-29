package de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerBaseImpl

import java.awt.Color
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point

class GameInitializer @Inject()(override val tuiMap: TuiMapInterface) extends GameInitializerInterface {
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

  val injector = Guice.createInjector(new ScotlandYardModule)

  override def initialize(nPlayers: Int = 3): GameModel = {
    val stations = initStations()
    GameModel(stations = stations, players = initPlayers(nPlayers, stations), gameRunning = true, stuckPlayers = Set[Player]())
  }
  
  def getColorList(): Vector[Color] = {
    this.colorList
  }

  def initDetectiveFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color, stations: Vector[Station]): Player = {
    val st = stations(stationNumber)
    Detective(name = name, station = st, color = color, tickets = tickets)
  }

  override def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: List[TicketType], stations: Vector[Station]): MrX = {
    MrX(station = stations(stationNumber), name = name, isVisible = isVisible, lastSeen = lastSeen, tickets = tickets, history = history)
  }

  private def initStations(): Vector[Station] = {
    val source: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val json = Json.parse(source)

    val jsonStations = json.as[JsArray].value
    val stationsBuffer = new ListBuffer[Station]()

    stationsBuffer += new Station(0, StationType.Taxi)

    // First loop over json file to create all Station objects
    for(jsonStation <- jsonStations ) {
      val stationType = StationType.fromString((jsonStation \ "type").as[String])
      val station = Station(number = (jsonStation \ "number").as[Int], stationType = stationType, tuiCoordinates = new Point((jsonStation \ "tuiCoordinates" \ "x").as[Int], (jsonStation \ "tuiCoordinates" \ "y").as[Int]), guiCoordinates = new Point((jsonStation \ "guiCoordinates" \ "x").as[Int], (jsonStation \ "guiCoordinates" \ "y").as[Int]))
      stationsBuffer += station
    }

    var stations = stationsBuffer.toVector.sortWith((s: Station, t: Station) => s.number < t.number)

    // Second loop over json file to set all neighbours. This needs to run after the first loop because all stations need to be created before getting assigned as neighbours
    for((jsonStation, index) <- jsonStations.zipWithIndex) {
      var station = stations(index + 1)
      station = station.setNeighbourTaxis(station, getNeighboursFor("taxi", jsonStation, stations))
      stations = stations.updated(index + 1, station)

      station = station.setNeighbourBuses(station, getNeighboursFor("bus", jsonStation, stations))
      stations = stations.updated(index + 1, station)

      station = station.setNeighbourUndergrounds(station, getNeighboursFor("underground", jsonStation, stations))
      stations = stations.updated(index + 1, station)
    }
    stations
  }

  private def getNeighboursFor(transportType: String, jsonStation: JsValue, stations: Vector[Station]): Set[Station] = {
    val neighboursIntList = (jsonStation \ "neighbours" \ transportType).as[List[Int]]
    var neighboursSet = Set[Station]()
    for(number <- neighboursIntList) {
      neighboursSet += stations(number)
    }
    neighboursSet
  }

  private def initPlayers(nPlayer: Int, stations: Vector[Station]): Vector[Player] = {
    val mrX = MrX(history = List(), station = stations(drawMisterXPosition()), tickets = Tickets(99, 99, 99, 5))
    var players = List[Player](mrX)
    for(i <- 1 until nPlayer) {
      val detective = Detective(station = stations(drawDetectivePosition()), name = "Dt" + i, color = colorList(i), Tickets(numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets))
      players = players ::: List(detective)
    }
    players.toVector
  }

  private def drawMisterXPosition(nonRandomPosition: Integer = -1): Int = {
    if(nonRandomPosition != -1) {
      return nonRandomPosition
    }
    val startPosIndex = r.nextInt(MAX_MISTERX_LIST_INDEX)
    misterXStartPositions(startPosIndex)
  }

  private def drawDetectivePosition(nonRandomPosition: Integer = -1): Int = {
    if (nonRandomPosition != -1) {
      return nonRandomPosition
    }
    var startPosIndex = 0
    do {
      startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    }
    while (drawnPositions.contains(startPosIndex))
      drawnPositions = drawnPositions :+ startPosIndex
    detectiveStartPositions(startPosIndex)
  }
}
