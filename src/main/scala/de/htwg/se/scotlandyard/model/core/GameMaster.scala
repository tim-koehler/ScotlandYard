package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.player._

object GameMaster {
  val defaultStation = new Station(0, StationType.Taxi, null, null, null)
  var stations: List[Station] = List()
  var players: List[Player] = List()
  var round = 1

  def startGame(): Boolean = {
    if(!GameInitializer.initialize()) {
      return false
    }
    true
  }

  def getCurrentPlayer(): Player = {
    players(getCurrentPlayerIndex() - 1)
  }

  def getCurrentPlayerIndex(): Int = {
    if(round % players.length == 0) {
      players.length
    } else {
      round % players.length
    }
  }

  def nextRound(): Unit = {
    round += 1
  }

  def validateMove(newPosition: Int): Boolean = {
    //TODO: check if player move is valid
    true
  }

  def updatePlayerPosition(newPosition: Int): Unit = {
    //TODO: getCurrentPlayer().station = GameMap.stations(newPosition)
    getCurrentPlayer().station.number = newPosition
  }

}
