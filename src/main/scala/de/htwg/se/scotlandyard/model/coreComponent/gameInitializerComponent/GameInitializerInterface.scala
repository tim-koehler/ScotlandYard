package de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent

import java.awt.Color

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.stationInitializerComponent.StationInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Tickets

trait GameInitializerInterface {

  val stationInitializer: StationInitializerInterface
  val tuiMap: TuiMapInterface

  val MRX_COLOR: Color
  val DT1_COLOR: Color
  val DT2_COLOR: Color
  val DT3_COLOR: Color
  val DT4_COLOR: Color
  val DT5_COLOR: Color
  val DT6_COLOR: Color

  val detectiveStartPositions: List[Int]
  val misterXStartPositions: List[Int]
  var MAX_DETECTIVE_LIST_INDEX: Int
  var MAX_MISTERX_LIST_INDEX: Int
  var drawnPositions: List[Int]
  val numberOfTaxiTickets: Int
  val numberOfBusTickets: Int
  val numberOfUndergroundTickets: Int
  val r = scala.util.Random

  def initialize(nPlayers: Int): Boolean

  def initDetectivesFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color): Boolean
  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: List[TicketType]): Boolean
}
