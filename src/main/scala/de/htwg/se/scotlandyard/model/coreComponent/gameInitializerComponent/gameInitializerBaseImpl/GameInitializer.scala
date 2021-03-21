package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerBaseImpl

import java.awt.Color
import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.model.Tickets
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType

import scala.collection.mutable

class GameInitializer @Inject()(override val stationInitializer: StationInitializerInterface,
                                override val tuiMap: TuiMapInterface) extends GameInitializerInterface {

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

  override def initialize(nPlayers: Int): Boolean = {
    GameMaster.stations = stationInitializer.initStations()
    initPlayers(nPlayers)
    true
  }
  
  def getColorList(): List[Color] = {
    this.colorList
  }

  def initDetectivesFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color): Boolean = {
    val st = GameMaster.stations(stationNumber)
    val detective = injector.getInstance(classOf[DetectiveInterface])
    detective.name = name
    detective.station = st
    detective.color = color
    detective.tickets = tickets

    GameMaster.players = GameMaster.players:::List(detective)
    tuiMap.updatePlayerPositions()
    true
  }

  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: mutable.Stack[TicketType]): Boolean = {
    GameMaster.players = List()
    val st = GameMaster.stations(stationNumber)
    GameMaster.players = List[DetectiveInterface](injector.getInstance(classOf[MrXInterface]))
    GameMaster.getMrX().station = st
    GameMaster.getMrX().name = name
    GameMaster.getMrX().isVisible = isVisible
    GameMaster.getMrX().lastSeen = lastSeen
    GameMaster.getMrX().tickets = tickets
    GameMaster.getMrX().history = history
    tuiMap.updatePlayerPositions()
    true
  }

  private def initPlayers(nPlayer: Int): Boolean = {
    GameMaster.players = List()

    var st = GameMaster.stations(drawMisterXPosition())
    val mrX = injector.getInstance(classOf[MrXInterface])
    mrX.history = mutable.Stack()
    mrX.station = st

    GameMaster.players = List[DetectiveInterface](mrX)
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
    GameMaster.getMrX().tickets = Tickets(99, 99, 99, 5)
    true
  }

  private def distributeTicketsToDetectives(): Boolean = {
    var success = false
    for(i <- 1 to (GameMaster.players.length - 1)) {
      success = distributeTickets(i, numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets)
    }
    success
  }

  private def distributeTickets(index: Int, nTaxi: Int, nBus: Int, nUnder: Int): Boolean = {
    GameMaster.players(index).tickets = Tickets(nTaxi, nBus, nUnder)
    true
  }
}
