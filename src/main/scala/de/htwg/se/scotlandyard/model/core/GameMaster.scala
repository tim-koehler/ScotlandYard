package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.player._


object GameMaster {
  val defaultStation = new Station(0, StationType.Taxi, null, null, null)
  var player1 = new MrX(defaultStation)
  var player2 = new Detective(defaultStation)
  var players: List[Player] = List(player1, player2)
  var round = 1

  def addDefaultPlayers(n: Int): Unit = {
    players =  List(player1)
    for(i <- 1 to (n - 1)) {
      players = new Detective(defaultStation, "Dt" + i.toString) :: players
    }
    players = players.reverse
  }

  def startGame(): Boolean = {
    if(!GameInitializer.initialize()) {
      false
    }
    true
  }

  def getCurrentPlayer(): Player = {
    players(getCurrentPlayerIndex())
  }

  def getCurrentPlayerIndex(): Int = {
    if(round == players.length) {
      return (players.length - 1)
    }
    (round % players.length) - 1
  }

  def nextRound(): Unit = {
    round += 1
  }

  def validateMove(newPosition: Int): Boolean = {
    //TODO: check if player move is valid
    true
  }

  def updatePlayerPosition(newPosition: Int): Unit = {
    //TODO: getCurrentPlayer().station = Map.stations(newPosition)
    getCurrentPlayer().station.number = newPosition
  }

}
