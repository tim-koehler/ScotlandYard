package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.Detective
import de.htwg.se.scotlandyard.util.Observable

class Controller extends Observable {

  def setPlayerNames(inputName: String, index: Int): Boolean = {
    if(index > GameMaster.players.length) {
      notifyObservers
      return false
    }
    val playerWithNewName = GameMaster.players(index)
    playerWithNewName.name = inputName
    GameMaster.players.updated(index, playerWithNewName)
    notifyObservers
    true
  }

  def setPlayerNumber(nPlayer: Int): Unit = {
    if(nPlayer < GameMaster.players.length) {
      reducePlayerNumber(nPlayer)
    } else {
      increasePlayerNumber(nPlayer)
    }
    notifyObservers
  }

  private def increasePlayerNumber(nPlayer: Int): Unit = {
    GameMaster.addDefaultPlayers(nPlayer)
  }

  private def reducePlayerNumber(nPlayer: Int): Unit = {
    val len = GameMaster.players.length
    GameMaster.players = GameMaster.players.dropRight(len - nPlayer)
  }

}
