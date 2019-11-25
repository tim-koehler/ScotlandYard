package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.util.Observable

class Controller extends Observable {

  def setPlayerNames(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    if(index < GameMaster.players.length) {
      returnValue = GameMaster.players(index).setPlayerName(inputName)
    }
    notifyObservers
    returnValue
  }

  def getPlayersList(): List[Player] = {
    GameMaster.players
  }

  def setPlayerNumber(nPlayer: Int): Int = {
    if(nPlayer < GameMaster.players.length) {
      reducePlayerNumber(nPlayer)
    }
    notifyObservers
    GameMaster.players.length
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

  def updateMrXVisibility(): Boolean = {
    GameMaster.updateMrXVisibility()
  }

  private def reducePlayerNumber(nPlayer: Int): Unit = {
    val len = GameMaster.players.length
    GameMaster.players = GameMaster.players.dropRight(len - nPlayer)
  }

}
