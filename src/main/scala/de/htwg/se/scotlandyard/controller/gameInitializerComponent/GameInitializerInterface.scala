package de.htwg.se.scotlandyard.controller.gameInitializerComponent

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, Tickets}

import java.awt.Color
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}

import scala.collection.mutable

trait GameInitializerInterface {
  val tuiMap: TuiMapInterface

  val MRX_COLOR: Color
  val DT1_COLOR: Color
  val DT2_COLOR: Color
  val DT3_COLOR: Color
  val DT4_COLOR: Color
  val DT5_COLOR: Color
  val DT6_COLOR: Color

  val detectiveStartPositions: Vector[Int]
  val misterXStartPositions: Vector[Int]
  var MAX_DETECTIVE_LIST_INDEX: Int
  var MAX_MISTERX_LIST_INDEX: Int
  var drawnPositions: Vector[Int]
  val numberOfTaxiTickets: Int
  val numberOfBusTickets: Int
  val numberOfUndergroundTickets: Int
  val r = scala.util.Random

  def initialize(nPlayer: Int): GameModel

  def initDetectiveFromLoad(name: String, stationNumber: Int, tickets: Tickets, color: Color, stations: Vector[Station]): DetectiveInterface
  def getColorList(): Vector[Color]
  def initMrXFromLoad(name: String, stationNumber: Int, isVisible: Boolean, lastSeen: String, tickets: Tickets, history: mutable.Stack[TicketType], stations: Vector[Station]): MrXInterface
}
