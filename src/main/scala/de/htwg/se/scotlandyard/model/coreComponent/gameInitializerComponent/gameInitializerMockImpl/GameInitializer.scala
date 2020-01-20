package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.stationInitializerMockImpl.StationInitializer
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.{Detective, MrX}
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Tickets

class GameInitializer() extends GameInitializerInterface{
  override val MRX_COLOR: Color = Color.BLACK
  override val DT1_COLOR: Color = Color.BLUE
  override val DT2_COLOR: Color = Color.RED
  override val DT3_COLOR: Color = Color.GREEN
  override val DT4_COLOR: Color = Color.PINK
  override val DT5_COLOR: Color = Color.YELLOW
  override val DT6_COLOR: Color = Color.CYAN
  override val detectiveStartPositions: List[Int] = List(1)
  override val misterXStartPositions: List[Int] = List(3)
  override var drawnPositions: List[Int] = _
  override val numberOfTaxiTickets: Int = 5
  override val numberOfBusTickets: Int = 3
  override val numberOfUndergroundTickets: Int = 1

  override def initialize(nPlayers: Int): Boolean = {
    GameMaster.players = List(new MrX())
    for(i <- 1 to (nPlayers - 1)) {
      GameMaster.players = GameMaster.players:::List(new Detective)
    }
    true
  }

  override var MAX_DETECTIVE_LIST_INDEX: Int = 0
  override var MAX_MISTERX_LIST_INDEX: Int = 2

  override def initDetectivesFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color): Boolean = true

  override def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: List[TicketType]): Boolean = true

  override val stationInitializer: StationInitializerInterface = new StationInitializer()
  override val tuiMap: TuiMapInterface = new TuiMap()
}