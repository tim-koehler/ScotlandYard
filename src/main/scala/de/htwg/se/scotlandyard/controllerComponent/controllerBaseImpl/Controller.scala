package de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl

import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.ScotlandYardModule
import de.htwg.se.scotlandyard.controllerComponent.{ControllerInterface, MoveCommand, NumberOfPlayersChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.UndoManager

import scala.swing.Publisher

class Controller @Inject() extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager()

  val injector = Guice.createInjector(new ScotlandYardModule)
  override var gameInitializer = injector.getInstance(classOf[GameInitializerInterface])
  override var fileIO = injector.getInstance(classOf[FileIOInterface])

  def load(): Boolean = {
    val fileIo = fileIO
    fileIo.load()
    true
  }

  def save(): Boolean = {
    val fileIo = fileIO
    fileIo.save()
    true
  }

  def initPlayers(nPlayer: Int): Integer = {
    GameMaster.initialize(nPlayer, gameInitializer)
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
