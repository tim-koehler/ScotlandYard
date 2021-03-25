package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}

import scala.collection.mutable

case class GameModel(
                      stations: List[Station] = List(),
                      players: List[DetectiveInterface] = List(),
                      round: Int =  1, // counter of moves (increases by 1 when a player moved)
                      totalRound: Int =  1, // number of total rounds (increases by 1 when every player has moved once)
                      win: Boolean = false,
                      gameRunning: Boolean = false,
                      winningPlayer: DetectiveInterface = new Detective,
                      stuckPlayers: Set[DetectiveInterface] = Set(),
                      WINNING_ROUND: Int = 24, //24
                      MRX_VISIBLE_ROUNDS: List[Int] = List(3, 8, 13, 18, 24)
                    ) {

  def getCurrentPlayer: DetectiveInterface = {
    players(getCurrentPlayerIndex)
  }

  def getLastPlayer: DetectiveInterface = {
    val index = getCurrentPlayerIndex
    if(index == 0) {
      players.last
    } else {
      players(index - 1)
    }
  }

  def getMrX: MrXInterface = players.head.asInstanceOf[MrXInterface]
  def getDetectives: List[DetectiveInterface] = players.drop(1)

  def getCurrentPlayerIndex: Integer = {
    if (round % players.length == 0) {
      players.length - 1
    } else {
      (round % players.length) - 1
    }
  }

  def increaseRound(): GameModel = {
    copy(round = round + 1)
  }

  def decreaseRound(): GameModel = {
    copy(round = round + 1)
  }

  def updateTotalRound(): GameModel = {
    copy(totalRound = (round.toDouble / players.length.toDouble).ceil.toInt)
  }

  def updatePlayerPosition(newPosition: Int): Station = {
    getCurrentPlayer.station = stations(newPosition)
    getCurrentPlayer.station
  }

  def addStuckPlayer(): GameModel = {
    copy(stuckPlayers = this.stuckPlayers + this.getCurrentPlayer)
  }

  def winGame(winningPlayer: DetectiveInterface): GameModel = {
    copy(winningPlayer = winningPlayer, gameRunning = false, win = true)
  }

  def incrementTickets(x: Int): Int = {x + 1}
  def decrementTickets(x: Int): Int = {x - 1}

  def updateTickets(ticketType: TicketType)(modFunc:Int => Int): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer.tickets.taxiTickets = modFunc(getCurrentPlayer.tickets.taxiTickets)
        getCurrentPlayer.tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer.tickets.busTickets = modFunc(getCurrentPlayer.tickets.busTickets)
        getCurrentPlayer.tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer.tickets.undergroundTickets = modFunc(getCurrentPlayer.tickets.undergroundTickets)
        getCurrentPlayer.tickets.undergroundTickets
      case _ =>
        getCurrentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets = modFunc(getCurrentPlayer.tickets.blackTickets)
        getCurrentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
}
