package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.util.Observable
import de.htwg.se.scotlandyard.util.UndoManager

class Controller extends Observable {

  private val undoManager = new UndoManager()

  def setPlayerNames(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    if(index < GameMaster.players.length || inputName.equals("")) {
      returnValue = GameMaster.players(index).setPlayerName(inputName)
    }
    notifyObservers
    returnValue
  }

  def getWinningPlayer(): Player = {
    GameMaster.winningPlayer
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

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    GameMaster.validateMove(newPosition, ticketType)
  }

  def doMove(newPosition: Int, ticketType: TicketType): Station = {
    val newStation = undoManager.doStep(new MoveCommand(getCurrentPlayer().getPosition().number, newPosition, ticketType))
    notifyObservers
    newStation
  }

  def undoValidateAndMove(): Station = {
    val newStation = undoManager.undoStep()
    notifyObservers
    newStation
  }

  def redoValidateAndMove(): Station = {
    val newStation = undoManager.redoStep()
    notifyObservers
    newStation
  }

  def updateMrXVisibility(): Boolean = {
    GameMaster.updateMrXVisibility()
  }

  def getWin(): Boolean = {
    GameMaster.win
  }
}
