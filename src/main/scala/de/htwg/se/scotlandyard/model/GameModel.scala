package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}

import scala.collection.mutable

object GameModel {

  var stations: List[Station] = List()
  var players: List[DetectiveInterface] = List()
  var round = 1 // counter of moves (increases by 1 when a player moved)
  var totalRound = 1 // number of total rounds (increases by 1 when every player has moved once)
  var win = false
  var gameRunning = false
  var winningPlayer: DetectiveInterface = _
  var stuckPlayers: mutable.Set[DetectiveInterface] = scala.collection.mutable.Set[DetectiveInterface]()

  val WINNING_ROUND = 24 //24
  val MRX_VISIBLE_ROUNDS: List[Int] = List(3, 8, 13, 18, 24)

  def getCurrentPlayer: DetectiveInterface = {
    players(getCurrentPlayerIndex())
  }

  def getLastPlayer: DetectiveInterface = {
    val index = getCurrentPlayerIndex()
    if(index == 0) {
      players.last
    } else {
      players(index - 1)
    }
  }

  def getMrX: MrXInterface = {
    players.head.asInstanceOf[MrXInterface]
  }

  def getDetectives: List[DetectiveInterface] = {
    players.drop(1)
  }

  def getCurrentPlayerIndex: Integer = {
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
    getCurrentPlayer.station = stations(newPosition)
    getCurrentPlayer.station
  }

  def incrementTickets(x: Int): Int = {x + 1}
  def decrementTickets(x: Int): Int = {x - 1}

  def updateTickets(ticketType: TicketType)(modFunc:Int => Int): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer.tickets.taxiTickets = modFunc(getCurrentPlayer.tickets.taxiTickets)
        getCurrentPlayer.tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer.tickets.busTickets = modFunc(getCurrentPlayer().tickets.busTickets)
        getCurrentPlayer.tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer.tickets.undergroundTickets = modFunc(getCurrentPlayer().tickets.undergroundTickets)
        getCurrentPlayer.tickets.undergroundTickets
      case _ =>
        getCurrentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets = modFunc(getCurrentPlayer().tickets.blackTickets)
        getCurrentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
}
