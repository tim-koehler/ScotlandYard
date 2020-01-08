package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface

class GameInitializer extends GameInitializerInterface{
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

  override def initialize(nPlayers: Int): Boolean = true

  override var MAX_DETECTIVE_LIST_INDEX: Int = 0
  override var MAX_MISTERX_LIST_INDEX: Int = 2
}
