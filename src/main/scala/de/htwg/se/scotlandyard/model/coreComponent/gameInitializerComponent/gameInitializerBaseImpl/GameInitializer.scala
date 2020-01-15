package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl

import java.awt.Color

import com.google.inject.Guice
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class GameInitializer extends GameInitializerInterface {

  val injector = Guice.createInjector(new ScotlandYardModule)

  val MRX_COLOR = Color.BLACK
  val DT1_COLOR = Color.BLUE
  val DT2_COLOR = Color.GREEN
  val DT3_COLOR = Color.ORANGE
  val DT4_COLOR = Color.MAGENTA
  val DT5_COLOR = Color.RED
  val DT6_COLOR = Color.CYAN

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

  override def initialize(nPlayers: Int): Boolean = {
    if(GameMaster.stations.size == 0){
      GameMaster.stations = injector.getInstance(classOf[StationInitializerInterface]).initStations()
    }
    initPlayers(nPlayers)
    true
  }

  def initDetectiveFromLoad(name: String, stationNumber: Int, taxiTickets: Int, busTickets: Int, undergroundTickets: Int, color: Color): Boolean = {
    val st = GameMaster.stations(stationNumber)
    val detective = injector.getInstance(classOf[DetectiveInterface])
    detective.name = name
    detective.station = st
    detective.color = color
    detective.taxiTickets = taxiTickets
    detective.busTickets = busTickets
    detective.undergroundTickets = undergroundTickets

    GameMaster.players = GameMaster.players:::List(detective)
    injector.getInstance(classOf[TuiMapInterface]).updatePlayerPositions()
    true
  }

  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, blackTickets: Int, doubleTurns: Int, taxiTickets: Int, busTickets: Int, undergroundTickets: Int, history: List[TicketType]): Boolean = {
    GameMaster.players = List()
    val st = GameMaster.stations(stationNumber)
    GameMaster.players = List[DetectiveInterface](injector.getInstance(classOf[MrXInterface]))
    GameMaster.getMrX().station = st
    GameMaster.getMrX().name = name
    GameMaster.getMrX().isVisible = isVisible
    GameMaster.getMrX().lastSeen = lastSeen
    GameMaster.getMrX().blackTickets = blackTickets
    GameMaster.getMrX().doubleTurn = doubleTurns
    GameMaster.getMrX().taxiTickets = taxiTickets
    GameMaster.getMrX().busTickets = busTickets
    GameMaster.getMrX().undergroundTickets = undergroundTickets
    GameMaster.getMrX().history = history
    injector.getInstance(classOf[TuiMapInterface]).updatePlayerPositions()
    true
  }

  private def initPlayers(nPlayer: Int): Boolean = {
    GameMaster.players = List()

    var st = GameMaster.stations(drawMisterXPosition())

    val mrx = injector.getInstance(classOf[MrXInterface])
    mrx.station = st

    GameMaster.players = List[DetectiveInterface](mrx)
    for(i <- 1 to (nPlayer - 1)) {
      st = GameMaster.stations(drawDetectivePosition())
      val detective = injector.getInstance(classOf[DetectiveInterface])
      detective.station = st
      detective.name = "Dt" + i
      detective.color = colorList(i)
      GameMaster.players = GameMaster.players:::List(detective)
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
    distributeTickets(0, 99, 99, 99)
  }

  private def distributeTicketsToDetectives(): Boolean = {
    var success = false
    for(i <- 1 to (GameMaster.players.length - 1)) {
      success = distributeTickets(i, numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets)
    }
    success
  }

  private def distributeTickets(index: Int, nTaxi: Int, nBus: Int, nUnder: Int): Boolean = {
    GameMaster.players(index).taxiTickets = nTaxi
    GameMaster.players(index).busTickets = nBus
    GameMaster.players(index).undergroundTickets = nUnder
    true
  }
}
