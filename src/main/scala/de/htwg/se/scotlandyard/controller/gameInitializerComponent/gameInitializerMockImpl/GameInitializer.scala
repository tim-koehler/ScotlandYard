package de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}

import java.awt.Color
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{Detective, MrX, Player}

import scala.collection.mutable

class GameInitializer() extends GameInitializerInterface{
  override val MRX_COLOR: Color = Color.BLACK
  override val DT1_COLOR: Color = Color.BLUE
  override val DT2_COLOR: Color = Color.RED
  override val DT3_COLOR: Color = Color.GREEN
  override val DT4_COLOR: Color = Color.PINK
  override val DT5_COLOR: Color = Color.YELLOW
  override val DT6_COLOR: Color = Color.CYAN
  override val detectiveStartPositions: Vector[Int] = Vector(1)
  override val misterXStartPositions: Vector[Int] = Vector(3)
  override var drawnPositions: Vector[Int] = _
  override val numberOfTaxiTickets: Int = 5
  override val numberOfBusTickets: Int = 3
  override val numberOfUndergroundTickets: Int = 1

  override def initialize(nPlayers: Int): GameModel = {
        GameModel()
  }

  override var MAX_DETECTIVE_LIST_INDEX: Int = 0
  override var MAX_MISTERX_LIST_INDEX: Int = 2

  override def initDetectiveFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color, stations: Vector[Station]): Player = Detective()
  override def getColorList(): Vector[Color] = Vector(MRX_COLOR, DT1_COLOR)

  override def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: List[TicketType], stations: Vector[Station]): MrX = MrX()
  override val tuiMap: TuiMapInterface = new TuiMap()
}
