package de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.controllerComponent.{ControllerInterface, LobbyChange, MoveCommand, MoveValidator, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame, UndoManager}
import de.htwg.se.scotlandyard.model.GameModel.{WINNING_ROUND, players, round, stations, stuckPlayers}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

import scala.swing.Publisher
import scala.util.control.Breaks.{break, breakable}

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
    GameModel.initialize(nPlayer)
    publish(new NumberOfPlayersChanged)
    GameModel.players.length
  }

  def nextRound(): Integer = {
    updateMrXVisibility()
    round += 1
    GameModel.updateTotalRound()
    if (!checkIfPlayerIsAbleToMove()) {
      stuckPlayers.add(GameModel.getCurrentPlayer())
      if (stuckPlayers.size == players.size - 1) {
        winGame(GameModel.getCurrentPlayer())
      } else {
        nextRound()
      }
    }
    round
  }

  def previousRound(): Integer = {
    updateMrXVisibility()
    round -= 1
    GameModel.updateTotalRound()
    round
  }

  private def checkIfPlayerIsAbleToMove(): Boolean = {
    GameModel.getCurrentPlayer().station.stationType match {
      case StationType.Taxi =>
        GameModel.getCurrentPlayer().tickets.taxiTickets > 0
      case model.StationType.Bus =>
        GameModel.getCurrentPlayer().tickets.taxiTickets > 0 || GameModel.getCurrentPlayer().tickets.busTickets > 0
      case model.StationType.Underground =>
        GameModel.getCurrentPlayer().tickets.taxiTickets > 0 || GameModel.getCurrentPlayer().tickets.busTickets > 0 || GameModel.getCurrentPlayer().tickets.undergroundTickets > 0
    }
  }

  private def checkDetectiveWin(): Boolean = {
    for (dt <- GameModel.getDetectives()) {
      if (dt.station.number == GameModel.getMrX().station.number) {
        return true
      }
    }
    false
  }

  private def checkMrXWin(): Boolean = {
    round == WINNING_ROUND * players.length
  }

  def move(newPosition: Int, ticketType: TicketType): Station = {
    if(MoveValidator.validateMove(newPosition, ticketType)) {
      val newStation = undoManager.doStep(new MoveCommand(GameModel.getCurrentPlayer().station.number, newPosition, ticketType))
      publish(new PlayerMoved)

      if (checkDetectiveWin()) {
        winGame(GameModel.getLastPlayer())
      }
      if (checkMrXWin()) {
        winGame(GameModel.getLastPlayer())
      }
      newStation
    } else {
      GameModel.getCurrentPlayer().station
    }
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
    val mrX = getMrX()
    mrX.isVisible = GameModel.checkMrXVisibility()
    if (mrX.isVisible) {
      mrX.lastSeen = players.head.station.number.toString
    }
    mrX.isVisible
  }

  def startGame(): Boolean = {
    publish(new StartGame)
    true
  }

  def winGame(winningPlayer: DetectiveInterface): Boolean = {
    GameModel.winningPlayer = winningPlayer
    GameModel.gameRunning = false
    GameModel.win = true
    publish(new PlayerWin)
    true
  }

  // Getters and Setters
  def getCurrentPlayer(): DetectiveInterface = {
    GameModel.getCurrentPlayer()
  }

  def getMrX(): MrXInterface = {
    GameModel.getMrX()
  }

  def getPlayersList(): List[DetectiveInterface] = {
    GameModel.players
  }

  def getStations(): List[Station] = {
    GameModel.stations
  }

  def getTotalRound(): Integer = {
    GameModel.totalRound
  }

  def getWin(): Boolean = {
    GameModel.win
  }

  def getGameRunning(): Boolean = {
    GameModel.gameRunning
  }

  def getWinningPlayer(): DetectiveInterface = {
    GameModel.winningPlayer
  }

  def setPlayerName(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    returnValue = GameModel.players(index).setPlayerName(inputName)
    publish(new PlayerNameChanged)
    returnValue
  }

  def setPlayerColor(newColor: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    returnValue = GameModel.players(index).setPlayerColor(newColor)
    publish(new PlayerColorChanged)
    returnValue
  }

  def updateLobby(): Boolean = {
    publish(new LobbyChange)
    true
  }
}
