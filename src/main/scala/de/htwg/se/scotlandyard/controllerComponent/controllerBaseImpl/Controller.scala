package de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.aview.tui.EnterNameState
import de.htwg.se.scotlandyard.controllerComponent.{ControllerInterface, LobbyChange, MoveCommand, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.UndoManager

import scala.swing.Publisher

class Controller @Inject()(override var gameInitializer: GameInitializerInterface,
                           override var fileIO: FileIOInterface) extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager()

  def load(): Boolean = {
    fileIO.load()
    true
  }

  def save(): Boolean = {
    fileIO.save()
    true
  }

  def initPlayers(nPlayer: Int): Integer = {
    GameMaster.initialize(nPlayer)
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
    val newStation = undoManager.doStep(new MoveCommand(getCurrentPlayer().station.number, newPosition, ticketType))

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
    GameMaster.gameRunning = false
    GameMaster.win = false
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

  def getGameRunning(): Boolean = {
    GameMaster.gameRunning
  }

  def getWinningPlayer(): DetectiveInterface = {
    GameMaster.winningPlayer
  }

  def setPlayerName(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    returnValue = GameMaster.players(index).setPlayerName(inputName)
    publish(new PlayerNameChanged)
    returnValue
  }

  def setPlayerColor(newColor: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    returnValue = GameMaster.players(index).setPlayerColor(newColor)
    publish(new PlayerColorChanged)
    returnValue
  }

  def updateLobby(): Boolean = {
    publish(new LobbyChange)
    true
  }

  def setWinning(win: Boolean): Boolean = {
    val oldWin = GameMaster.win
    GameMaster.win = win
    publish(new PlayerWin)
    oldWin
  }
}
