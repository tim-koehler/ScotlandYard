package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player._
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

  def getPlayersList(): List[Player] = {
    GameMaster.players
  }

  def setPlayerNumber(nPlayer: Int): Unit = {
    if(nPlayer < GameMaster.players.length) {
      reducePlayerNumber(nPlayer)
    }
    notifyObservers
  }

  def getCurrentPlayer(): Player = {
    GameMaster.getCurrentPlayer()
  }

  def nextRound(): Int = {
    GameMaster.nextRound()
  }

  def validateAndMove(newPosition: Int, ticketType: TicketType): Boolean = {
    if (GameMaster.validateMove(newPosition, ticketType)) {
      GameMaster.updatePlayerPosition(newPosition, ticketType)
      nextRound()
      notifyObservers
      return true
    }
    notifyObservers
    false
  }

  def updateTotalRound(): Int = {
    GameMaster.updateTotalRound()
  }

  def setMrXVisibility(): Boolean = {
    GameMaster.setMrXVisibility()
  }

  private def reducePlayerNumber(nPlayer: Int): Unit = {
    val len = GameMaster.players.length
    GameMaster.players = GameMaster.players.dropRight(len - nPlayer)
  }

}
