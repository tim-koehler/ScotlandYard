package de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl

import de.htwg.se.scotlandyard.controllerComponent.{ControllerInterface, NumberOfPlayersChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.core.fileIoComponent.fileIoJsonImpl.FileIO
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.UndoManager

import scala.swing.Publisher

class Controller extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager()
  //TODO: Dependency Injection with FileIO

  def load(): Unit = {
    val fileIo = new FileIO()
    fileIo.load()
  }

  def save(): Unit = {
    val fileIo = new FileIO()
    fileIo.save()
  }

  def initPlayers(nPlayer: Int): Integer = {
    GameInitializer.initPlayers(nPlayer)
    publish(new NumberOfPlayersChanged)
    GameMaster.players.length
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

    publish(new PlayerMoved)
    newStation
  }

  def undoValidateAndMove(): Station = {
    val newStation = undoManager.undoStep()
    publish(new PlayerMoved)
    newStation
  }

  def redoValidateAndMove(): Station = {
    val newStation = undoManager.redoStep()
    publish(new PlayerMoved)
    newStation
  }

  def updateMrXVisibility(): Boolean = {
    GameMaster.updateMrXVisibility()
  }

  def startGame(): Boolean = {
    publish(new StartGame)
    true
  }

  def winGame(): Boolean = {
    publish(new PlayerWin)
    true
  }
  // Getters and Setters
  def getCurrentPlayer(): DetectiveInterface = {
    GameMaster.getCurrentPlayer()
  }

  def getMrX(): MrXInterface = {
    GameMaster.getMrX()
  }

  def getPlayersList(): List[DetectiveInterface] = {
    GameMaster.players
  }

  def getStations(): List[Station] = {
    GameMaster.stations
  }

  def getTotalRound(): Integer = {
    GameMaster.totalRound
  }

  def getWin(): Boolean = {
    GameMaster.win
  }

  def getWinningPlayer(): DetectiveInterface = {
    GameMaster.winningPlayer
  }

  def setPlayerName(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    if(index < GameMaster.players.length || inputName.equals("")) {
      returnValue = GameMaster.players(index).setPlayerName(inputName)
    }
    publish(new PlayerNameChanged)
    returnValue
  }

  def setWinning(win: Boolean): Boolean = {
    val oldWin = GameMaster.win
    GameMaster.win = win
    publish(new PlayerWin)
    oldWin
  }
}
