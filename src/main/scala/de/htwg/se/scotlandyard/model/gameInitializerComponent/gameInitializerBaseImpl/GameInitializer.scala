package de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl

import java.awt.Color
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.swing.Point

class GameInitializer @Inject()(override val tuiMap: TuiMapInterface) extends GameInitializerInterface {
  val stationsJsonFilePath = "./resources/stations.json"
  val MRX_COLOR = Color.BLACK
  val DT1_COLOR = Color.BLUE
  val DT2_COLOR = Color.decode("#1c8c1c")
  val DT3_COLOR = Color.decode("#de991b")
  val DT4_COLOR = Color.MAGENTA
  val DT5_COLOR = Color.RED
  val DT6_COLOR = Color.decode("#2a9fcc")

  val colorList = List(MRX_COLOR, DT1_COLOR, DT2_COLOR, DT3_COLOR, DT4_COLOR, DT5_COLOR, DT6_COLOR)

  // real starting positions
  val detectiveStartPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174) // 16
  val misterXStartPositions = List(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172) // 13
  // drawn Detective Positions
  var drawnPositions: List[Int] = List()
  val numberOfTaxiTickets = 11
  val numberOfBusTickets = 8
  val numberOfUndergroundTickets = 4

  override var MAX_DETECTIVE_LIST_INDEX: Int = detectiveStartPositions.length -1
  override var MAX_MISTERX_LIST_INDEX: Int = misterXStartPositions.length -1

  val injector = Guice.createInjector(new ScotlandYardModule)

  override def initialize(nPlayers: Int = 3): Boolean = {
    GameModel.round = 1
    GameModel.totalRound = 1
    GameModel.win = false
    GameModel.gameRunning = true
    GameModel.stuckPlayers = scala.collection.mutable.Set[DetectiveInterface]()

    GameModel.stations = initStations()
    initPlayers(nPlayers)

    GameModel.players.head.asInstanceOf[MrXInterface].history = mutable.Stack()
    true
  }
  
  def getColorList(): List[Color] = {
    this.colorList
  }

  def initDetectivesFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color): Boolean = {
    val st = GameModel.stations(stationNumber)
    val detective = injector.getInstance(classOf[DetectiveInterface])
    detective.name = name
    detective.station = st
    detective.color = color
    detective.tickets = tickets

    GameModel.players = GameModel.players:::List(detective)
    tuiMap.updatePlayerPositions()
    true
  }

  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: mutable.Stack[TicketType]): Boolean = {
    GameModel.players = List()
    val st = GameModel.stations(stationNumber)
    GameModel.players = List[DetectiveInterface](injector.getInstance(classOf[MrXInterface]))
    GameModel.getMrX().station = st
    GameModel.getMrX().name = name
    GameModel.getMrX().isVisible = isVisible
    GameModel.getMrX().lastSeen = lastSeen
    GameModel.getMrX().tickets = tickets
    GameModel.getMrX().history = history
    tuiMap.updatePlayerPositions()
    true
  }

  private def initStations(): List[Station] = {
    val source: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
    val json = Json.parse(source)

    val jsonStations = json.as[JsArray].value
    val stationsBuffer = new ListBuffer[Station]()

    stationsBuffer += new Station(0, StationType.Taxi)

    // First loop over json file to create all Station objects
    for(jsonStation <- jsonStations ) {
      val stationType = StationType.fromString((jsonStation \ "type").as[String])
      val station = new Station((jsonStation \ "number").as[Int], stationType)
      station.tuiCoords = new Point((jsonStation \ "tuiCoordinates" \ "x").as[Int], (jsonStation \ "tuiCoordinates" \ "y").as[Int])
      station.guiCoords = new Point((jsonStation \ "guiCoordinates" \ "x").as[Int], (jsonStation \ "guiCoordinates" \ "y").as[Int])
      stationsBuffer += station
    }

    val stations = stationsBuffer.toList.sortWith((s: Station, t: Station) => s.number < t.number)

    // Second loop over json file to set all neighbours. This needs to run after the first loop because all stations need to be created before getting assigned as neighbours
    for((jsonStation, index) <- jsonStations.zipWithIndex) {
      stations(index + 1).setNeighbourTaxis(getNeighboursFor("taxi", jsonStation, stations))
      stations(index + 1).setNeighbourBuses(getNeighboursFor("bus", jsonStation, stations))
      stations(index + 1).setNeighbourUndergrounds(getNeighboursFor("underground", jsonStation, stations))
    }
    stations
  }

  private def getNeighboursFor(transportType: String, jsonStation: JsValue, stations: List[Station]): Set[Station] = {
    val neighboursIntList = (jsonStation \ "neighbours" \ transportType).as[List[Int]]
    var neighboursSet = Set[Station]()
    for(number <- neighboursIntList) {
      neighboursSet += stations(number)
    }
    neighboursSet
  }

  private def initPlayers(nPlayer: Int): Boolean = {
    GameModel.players = List()

    var st = GameModel.stations(drawMisterXPosition())
    val mrX = injector.getInstance(classOf[MrXInterface])
    mrX.history = mutable.Stack()
    mrX.station = st

    GameModel.players = List[DetectiveInterface](mrX)
    for(i <- 1 to (nPlayer - 1)) {
      st = GameModel.stations(drawDetectivePosition())
      val detective = injector.getInstance(classOf[DetectiveInterface])
      detective.station = st
      detective.name = "Dt" + i
      detective.color = colorList(i)
      GameModel.players = GameModel.players:::List(detective)
    }
    distributeTicketsToMrX()
    distributeTicketsToDetectives()
    drawnPositions = List()
    true
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
    drawnPositions = startPosIndex :: drawnPositions
    detectiveStartPositions(startPosIndex)
  }

  private def distributeTicketsToMrX(): Boolean = {
    GameModel.getMrX().tickets = Tickets(99, 99, 99, 5)
    true
  }

  private def distributeTicketsToDetectives(): Boolean = {
    var success = false
    for(i <- 1 to (GameModel.players.length - 1)) {
      success = distributeTickets(i, numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets)
    }
    success
  }

  private def distributeTickets(index: Int, nTaxi: Int, nBus: Int, nUnder: Int): Boolean = {
    GameModel.players(index).tickets = Tickets(nTaxi, nBus, nUnder)
    true
  }
}
