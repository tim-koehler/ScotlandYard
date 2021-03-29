package de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}

import java.awt.Color
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point

class GameInitializer() extends GameInitializerInterface{
  override val MRX_COLOR: Color = Color.BLACK
  override val DT1_COLOR: Color = Color.BLUE
  override val DT2_COLOR: Color = Color.RED
  override val DT3_COLOR: Color = Color.GREEN
  override val DT4_COLOR: Color = Color.PINK
  override val DT5_COLOR: Color = Color.YELLOW
  override val DT6_COLOR: Color = Color.CYAN
  override val detectiveStartPositions = Vector(8, 19, 20)
  override val misterXStartPositions: Vector[Int] = Vector(3)
  override var drawnPositions: Vector[Int] = _
  override val numberOfTaxiTickets: Int = 5
  override val numberOfBusTickets: Int = 3
  override val numberOfUndergroundTickets: Int = 1

  val colorList = Vector(MRX_COLOR, DT1_COLOR, DT2_COLOR, DT3_COLOR, DT4_COLOR, DT5_COLOR, DT6_COLOR)

  override def initialize(nPlayers: Int, stationsSource: String): GameModel = {

    val stations = initStations(stationsSource)
    val players = initPlayers(nPlayers, stations)

    GameModel(players = players, stations = stations, gameRunning = true)
  }

  private def initStations(stationsSource: String): Vector[Station] = {
    val json = Json.parse(stationsSource)

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

  private def initPlayers(nPlayer: Int, stations: Vector[Station]): Vector[Player] = {
    val mrX = MrX(history = List(), station = stations(1), tickets = Tickets(99, 99, 99, 5))
    var players = List[Player](mrX)
    for(i <- 1 until nPlayer) {
      val detective = Detective(station = stations(detectiveStartPositions(i - 1)), name = "Dt" + i, color = colorList(i), Tickets(numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets))
      players = players ::: List(detective)
    }
    players.toVector
  }

  private def getNeighboursFor(transportType: String, jsonStation: JsValue, stations: Vector[Station]): Set[Station] = {
    val neighboursIntList = (jsonStation \ "neighbours" \ transportType).as[List[Int]]
    var neighboursSet = Set[Station]()
    for(number <- neighboursIntList) {
      neighboursSet += stations(number)
    }
    neighboursSet
  }

  override var MAX_DETECTIVE_LIST_INDEX: Int = 0
  override var MAX_MISTERX_LIST_INDEX: Int = 2

  override def initDetectiveFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color, stations: Vector[Station]): Player = Detective()
  override def getColorList(): Vector[Color] = Vector(MRX_COLOR, DT1_COLOR)

  override def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: List[TicketType], stations: Vector[Station]): MrX = MrX()
  override val tuiMap: TuiMapInterface = new TuiMap()
}
