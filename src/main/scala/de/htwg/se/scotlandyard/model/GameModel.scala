package de.htwg.se.scotlandyard.model

import com.google.inject.Guice
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.{ScotlandYardModule, model}

import scala.collection.mutable

object GameModel {

  var gameInitializer: GameInitializerInterface = Guice.createInjector(new ScotlandYardModule).getInstance(classOf[GameInitializerInterface])

  val stations: List[Station] = gameInitializer.initStations()
  var players: List[DetectiveInterface] = List()
  var round = 1 // counter of moves (increases by 1 when a player moved)
  var totalRound = 1 // number of total rounds (increases by 1 when every player has moved once)
  var win = false
  var gameRunning = false
  var winningPlayer: DetectiveInterface = _
  var stuckPlayers: mutable.Set[DetectiveInterface] = scala.collection.mutable.Set[DetectiveInterface]()

  val WINNING_ROUND = 24 //24
  val MRX_VISIBLE_ROUNDS: List[Int] = List(3, 8, 13, 18, 24)

  def initialize(nPlayers: Int = 3): Boolean = {
    round = 1
    totalRound = 1
    win = false

    if (!gameInitializer.initialize(nPlayers)) {
      return false
    }

    players.head.asInstanceOf[MrXInterface].history = mutable.Stack()
    stuckPlayers = scala.collection.mutable.Set[DetectiveInterface]()
    gameRunning = true

    true
  }

  def getCurrentPlayer(): DetectiveInterface = {
    players(getCurrentPlayerIndex())
  }

  def getLastPlayer(): DetectiveInterface = {
    val index = getCurrentPlayerIndex()
    if(index == 0) {
      players.last
    } else {
      players(index - 1)
    }
  }

  def getMrX(): MrXInterface = {
    players.head.asInstanceOf[MrXInterface]
  }

  def getDetectives(): List[DetectiveInterface] = {
    players.drop(1)
  }

  def getCurrentPlayerIndex(): Integer = {
    if (round % players.length == 0) {
      players.length - 1
    } else {
      (round % players.length) - 1
    }
  }

  def updateTotalRound(): Integer = {
    totalRound = (round.toDouble / players.length.toDouble).ceil.toInt
    totalRound
  }

  def updatePlayerPosition(newPosition: Int): Station = {
    getCurrentPlayer().station = stations(newPosition)
    getCurrentPlayer().station
  }

  def checkMrXVisibility(): Boolean = {
    GameModel.MRX_VISIBLE_ROUNDS.contains(totalRound)
  }

  def decreaseTickets(ticketType: TicketType): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer().tickets.taxiTickets -= 1
        getCurrentPlayer().tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer().tickets.busTickets -= 1
        getCurrentPlayer().tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer().tickets.undergroundTickets -= 1
        getCurrentPlayer().tickets.undergroundTickets
      case _ =>
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets -= 1
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }

  def increaseTickets(ticketType: TicketType): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer().tickets.taxiTickets += 1
        getCurrentPlayer().tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer().tickets.busTickets += 1
        getCurrentPlayer().tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer().tickets.undergroundTickets += 1
        getCurrentPlayer().tickets.undergroundTickets
      case _ =>
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets += 1
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
}
