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

  def updatePlayerPosition(gameModel: GameModel, newPosition: Int): GameModel = {
    val newPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round).setPlayerStation(stations(newPosition))
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

  def updateTickets(gameModel: GameModel, ticketType: TicketType)(modFunc:Int => Int): GameModel = {
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    ticketType match {
      case TicketType.Taxi =>
        val newPlayer = currentPlayer.setPlayerTickets(currentPlayer.tickets.copy(taxiTickets = modFunc(currentPlayer.tickets.taxiTickets)))
        gameModel.copy(players = gameModel.players.updated(gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round), newPlayer))
      case TicketType.Bus =>
        val newPlayer = currentPlayer.setPlayerTickets(currentPlayer.tickets.copy(busTickets = modFunc(currentPlayer.tickets.busTickets)))
        gameModel.copy(players = gameModel.players.updated(gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round), newPlayer))
      case TicketType.Underground =>
        val newPlayer = currentPlayer.setPlayerTickets(currentPlayer.tickets.copy(undergroundTickets = modFunc(currentPlayer.tickets.undergroundTickets)))
        gameModel.copy(players = gameModel.players.updated(gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round), newPlayer))
      case _ =>
        val newPlayer = currentPlayer.setPlayerTickets(currentPlayer.tickets.copy(blackTickets = modFunc(currentPlayer.tickets.blackTickets)))
        gameModel.copy(players = gameModel.players.updated(gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round), newPlayer))
    }
  }
}
