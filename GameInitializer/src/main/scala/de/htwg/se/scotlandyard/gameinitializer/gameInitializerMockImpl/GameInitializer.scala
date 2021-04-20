package de.htwg.se.scotlandyard.gameinitializer.gameInitializerMockImpl

import de.htwg.se.scotlandyard.gameinitializer.GameInitializerInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, Tickets}

import java.awt.Color
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}

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
  override val detectiveStartPositions = Vector(2, 3)
  override val misterXStartPositions: Vector[Int] = Vector(1)
  override var drawnPositions: Vector[Int] = _
  override val numberOfTaxiTickets: Int = 5
  override val numberOfBusTickets: Int = 3
  override val numberOfUndergroundTickets: Int = 1

  val colorList = Vector(MRX_COLOR, DT1_COLOR, DT2_COLOR, DT3_COLOR, DT4_COLOR, DT5_COLOR, DT6_COLOR)

  override def initialize(nPlayers: Int, stationsSource: String): GameModel = {
    val station0 = Station(0)
    var station1 = Station(1, StationType.Underground, blackStation = false, Set(), Set(), Set())
    var station2 = Station(2, StationType.Taxi, blackStation = false, Set(1), Set(), Set())
    val station3 = Station(3, StationType.Taxi, blackStation = false, Set(1), Set(), Set())
    val station4 = Station(4, StationType.Bus, blackStation = false, Set(1), Set(), Set())
    var station5 = Station(5, StationType.Underground, blackStation = false, Set(1, 2), Set(), Set())
    station1 = station1.copy(neighbourTaxis = Set(2, 3, 4, 5), neighbourBuses = Set(5, 4), neighbourUndergrounds = Set(5))
    station2 = station2.copy(neighbourTaxis = Set(2, 3, 4, 5), neighbourBuses = Set(5, 4), neighbourUndergrounds = Set(5))
    station5 = station5.copy(neighbourTaxis = Set(1, 2, 3, 4), neighbourBuses = Set(1, 4), neighbourUndergrounds = Set(1))
    val stations: Vector[Station] = Vector(station0, station1, station2, station3, station4, station5)

    val player1 = MrX(station = station1)
    val player2 = Detective(station = station2, tickets = Tickets(11, 8, 4))
    val player3 = Detective(station = station3, tickets = Tickets(11, 8, 4))

    val players: Vector[Player] = Vector(player1, player2, player3)

    GameModel(stations = stations, players = players)
  }

  override var MAX_DETECTIVE_LIST_INDEX: Int = 0
  override var MAX_MISTERX_LIST_INDEX: Int = 2

  override def getColorList(): Vector[Color] = Vector(MRX_COLOR, DT1_COLOR)
}
