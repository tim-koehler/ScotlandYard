package de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl

import java.awt.Color
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
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

  override def initialize(nPlayers: Int = 3): GameModel = {
    val stations = initStations()
    GameModel(stations = stations, players = initPlayers(nPlayers, stations), gameRunning = true, stuckPlayers = Set[DetectiveInterface]())
  }
  
  def getColorList(): List[Color] = {
    this.colorList
  }

  def initDetectiveFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color, stations: List[Station]): DetectiveInterface = {
    val st = stations(stationNumber)
    val detective = injector.getInstance(classOf[DetectiveInterface])
    detective.name = name
    detective.station = st
    detective.color = color
    detective.tickets = tickets

    detective
  }

  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: mutable.Stack[TicketType], stations: List[Station]): MrXInterface = {
    val mrx = injector.getInstance(classOf[MrXInterface])
    mrx.station = stations(stationNumber)
    mrx.name = name
    mrx.isVisible = isVisible
    mrx.lastSeen = lastSeen
    mrx.tickets = tickets
    mrx.history = history
    mrx
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

  private def initPlayers(nPlayer: Int, stations: List[Station]): List[DetectiveInterface] = {
    var st = stations(drawMisterXPosition())
    val mrX = injector.getInstance(classOf[MrXInterface])
    mrX.history = mutable.Stack()
    mrX.station = st

    var players = List[DetectiveInterface](mrX)
    for(i <- 1 to (nPlayer - 1)) {
      st = stations(drawDetectivePosition())
      val detective = injector.getInstance(classOf[DetectiveInterface])
      detective.station = st
      detective.name = "Dt" + i
      detective.color = colorList(i)
      players = players ::: List(detective)
    }
    distributeTicketsToMrX(mrX)
    distributeTicketsToDetectives(players)
    drawnPositions = List()
    players.head.asInstanceOf[MrXInterface].history = mutable.Stack()
    players
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

  private def distributeTicketsToMrX(mrx: MrXInterface): Boolean = {
    mrx.tickets = Tickets(99, 99, 99, 5)
    true
  }

  private def distributeTicketsToDetectives(players: List[DetectiveInterface]): Boolean = {
    for(index <- 1 to (players.length - 1)) {
      players(index).tickets = Tickets(numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets)
    }
    true
  }
}
