package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}

import scala.collection.mutable

case class GameModel(
                      stations: Vector[Station] = Vector(),
                      players: Vector[DetectiveInterface] = Vector(),
                      round: Int = 1, // counter of moves (increases by 1 when a player moved)
                      totalRound: Int = 1, // number of total rounds (increases by 1 when every player has moved once)
                      win: Boolean = false,
                      gameRunning: Boolean = false,
                      winningPlayer: DetectiveInterface = new Detective,
                      stuckPlayers: Set[DetectiveInterface] = Set(),
                      allPlayerStuck: Boolean = false,
                      WINNING_ROUND: Int = 24, //24
                      MRX_VISIBLE_ROUNDS: Vector[Int] = Vector(3, 8, 13, 18, 24)
                    ) {

  def getCurrentPlayer(players: Vector[DetectiveInterface], round: Int): DetectiveInterface = {
    players(getCurrentPlayerIndex(players, round))
  }

  def getPreviousPlayer(players: Vector[DetectiveInterface], round: Int): DetectiveInterface = {
    val index = getCurrentPlayerIndex(players, round)
    if(index == 0) {
      players.last
    } else {
      players(index - 1)
    }
  }

  def getMrX(players: Vector[DetectiveInterface]): MrXInterface = players.head.asInstanceOf[MrXInterface]
  def getDetectives(players: Vector[DetectiveInterface]): Vector[DetectiveInterface] = players.drop(1)

  def getCurrentPlayerIndex(players: Vector[DetectiveInterface], round: Int): Int = {
    if (round % players.length == 0) {
      players.length - 1
    } else {
      (round % players.length) - 1
    }
  }

  def updatePlayerPosition(currentPlayer: DetectiveInterface, newPosition: Int): Station = {
    currentPlayer.station = stations(newPosition)
    currentPlayer.station
  }

  def updateRound(gameModel: GameModel, modFunc:Int => Int): GameModel = {
    val round = modFunc(gameModel.round)
    val totalRound = (round.toDouble / gameModel.players.length.toDouble).ceil.toInt
    gameModel.copy(round = round, totalRound = totalRound)
  }

  def addStuckPlayer(gameModel: GameModel, player: DetectiveInterface): GameModel = {
    gameModel.copy(stuckPlayers = gameModel.stuckPlayers + player)
  }

  def setAllPlayerStuck(gameModel: GameModel): GameModel = {
    gameModel.copy(allPlayerStuck = true)
  }

  def winGame(gameModel: GameModel, winningPlayer: DetectiveInterface): GameModel = {
    gameModel.copy(winningPlayer = winningPlayer, gameRunning = false, win = true)
  }

  def incrementValue(x: Int): Int = {x + 1}
  def decrementValue(x: Int): Int = {x - 1}

  def updateTickets(currentPlayer: DetectiveInterface, ticketType: TicketType)(modFunc:Int => Int): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        currentPlayer.tickets.taxiTickets = modFunc(currentPlayer.tickets.taxiTickets)
        currentPlayer.tickets.taxiTickets
      case TicketType.Bus =>
        currentPlayer.tickets.busTickets = modFunc(currentPlayer.tickets.busTickets)
        currentPlayer.tickets.busTickets
      case TicketType.Underground =>
        currentPlayer.tickets.undergroundTickets = modFunc(currentPlayer.tickets.undergroundTickets)
        currentPlayer.tickets.undergroundTickets
      case _ =>
        currentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets = modFunc(currentPlayer.tickets.blackTickets)
        currentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
}
