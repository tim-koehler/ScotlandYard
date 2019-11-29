package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.util.Observable

class Controller extends Observable {

  def setPlayerNames(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    if(index < GameMaster.players.length || inputName.equals("")) {
      returnValue = GameMaster.players(index).setPlayerName(inputName)
    }
    notifyObservers
    returnValue
  }

  def getPlayersList(): List[Player] = {
    GameMaster.players
  }

  def initPlayers(nPlayer: Int): Int = {
    GameInitializer.initPlayers(nPlayer)
    notifyObservers
    GameMaster.players.length
  }

  def getCurrentPlayer(): Player = {
    GameMaster.getCurrentPlayer()
  }

  def getTotalRound(): Int = {
    GameMaster.totalRound
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
}
