package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.util.Observable
import de.htwg.se.scotlandyard.util.UndoManager

class Controller extends Observable {

  val undoManager = new UndoManager()

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

  def initPlayers(nPlayer: Int): Integer = {
    GameInitializer.initPlayers(nPlayer)
    notifyObservers
    GameMaster.players.length
  }

  def getCurrentPlayer(): Player = {
    GameMaster.getCurrentPlayer()
  }

  def getTotalRound(): Integer = {
    GameMaster.totalRound
  }

  def nextRound(): Integer = {
    GameMaster.nextRound()
  }

  def previousRound(): Integer = {
    GameMaster.previousRound()
  }

  def validateAndMove(newPosition: Int, ticketType: TicketType): Boolean = {
    undoManager.doStep(new ValidateAndMoveCommand(this, newPosition, ticketType))
  }

  def updateMrXVisibility(): Boolean = {
    GameMaster.updateMrXVisibility()
  }
}
