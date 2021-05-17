package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}

case class PersistenceGameModel(
                                 players: Vector[Player] = Vector[Player](MrX(), Detective(name = "Dt1"), Detective(name = "Dt2")),
                                 round: Int = 1, // counter of moves (increases by 1 when a player moved)
                                 totalRound: Int = 1, // number of total rounds (increases by 1 when every player has moved once)
                                 win: Boolean = false,
                                 gameRunning: Boolean = false,
                                 winningPlayer: Player = Detective(),
                                 allPlayerStuck: Boolean = false,
                                 WINNING_ROUND: Int = 24, //24
                                 MRX_VISIBLE_ROUNDS: Vector[Int] = Vector(3, 8, 13, 18, 24)
                               ) {

  def toGameModel(stations: Vector[Station]): GameModel = {
    GameModel(stations, players, round, totalRound, win, gameRunning, winningPlayer, allPlayerStuck, WINNING_ROUND, MRX_VISIBLE_ROUNDS)
  }
}

