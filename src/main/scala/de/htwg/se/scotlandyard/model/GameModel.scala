package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.{Detective, MrX, Player}

import scala.collection.mutable

case class GameModel(
                      stations: Vector[Station] = Vector[Station](),
                      players: Vector[Player] = Vector[Player](),
                      round: Int = 1, // counter of moves (increases by 1 when a player moved)
                      totalRound: Int = 1, // number of total rounds (increases by 1 when every player has moved once)
                      win: Boolean = false,
                      gameRunning: Boolean = false,
                      winningPlayer: Player = Detective(),
                      stuckPlayers: Set[Player] = Set(),
                      allPlayerStuck: Boolean = false,
                      WINNING_ROUND: Int = 24, //24
                      MRX_VISIBLE_ROUNDS: Vector[Int] = Vector(3, 8, 13, 18, 24)
                    ) {

  def getCurrentPlayer(players: Vector[Player], round: Int): Player = {
    players(getCurrentPlayerIndex(players, round))
  }

  def getPreviousPlayer(players: Vector[Player], round: Int): Player = {
    val index = getCurrentPlayerIndex(players, round)
    if(index == 0) {
      players.last
    } else {
      players(index - 1)
    }
  }

  def getMrX(players: Vector[Player]): MrX = players.head.asInstanceOf[MrX]
  def getDetectives(players: Vector[Player]): Vector[Player] = players.drop(1)

  def getCurrentPlayerIndex(players: Vector[Player], round: Int): Int = {
    if (round % players.length == 0) {
      players.length - 1
    } else {
      (round % players.length) - 1
    }
  }

  def updatePlayerPosition(gameModel: GameModel, currentPlayer: Player, newPosition: Int): GameModel = {
    val newPlayer = currentPlayer.setPlayerStation(stations(newPosition))
    val newPlayers = gameModel.players.updated(getCurrentPlayerIndex(gameModel.players, gameModel.round), newPlayer)
    gameModel.copy(players = newPlayers)
  }

  def updateRound(gameModel: GameModel, modFunc:Int => Int): GameModel = {
    val round = modFunc(gameModel.round)
    val totalRound = (round.toDouble / gameModel.players.length.toDouble).ceil.toInt
    gameModel.copy(round = round, totalRound = totalRound)
  }

  def addStuckPlayer(gameModel: GameModel, player: Player): GameModel = {
    gameModel.copy(stuckPlayers = gameModel.stuckPlayers + player)
  }

  def setAllPlayerStuck(gameModel: GameModel): GameModel = {
    gameModel.copy(allPlayerStuck = true)
  }

  def winGame(gameModel: GameModel, winningPlayer: Player): GameModel = {
    gameModel.copy(winningPlayer = winningPlayer, gameRunning = false, win = true)
  }

  def incrementValue(x: Int): Int = {x + 1}
  def decrementValue(x: Int): Int = {x - 1}

  def updateTickets(currentPlayer: Player, ticketType: TicketType)(modFunc:Int => Int): Integer = {
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
        currentPlayer.asInstanceOf[MrX].tickets.blackTickets = modFunc(currentPlayer.tickets.blackTickets)
        currentPlayer.asInstanceOf[MrX].tickets.blackTickets
    }
  }
}
