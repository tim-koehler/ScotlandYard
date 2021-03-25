package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.controller.{ControllerInterface, LobbyChange, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

import java.awt.Color
import scala.swing.Publisher
import scala.util.control.Breaks.{break, breakable}

class Controller @Inject()(override val gameInitializer: GameInitializerInterface,
                           override val fileIO: FileIOInterface,
                           private var gameModel: GameModel) extends ControllerInterface with Publisher {

  private val undoManager = new UndoManager(gameModel)

  def load(): Boolean = {
    fileIO.load()
    true
  }

  def save(): Boolean = {
    fileIO.save(gameModel)
    true
  }

  override def initialize(nPlayers: Int = 3): Int = {
    gameInitializer.initialize(nPlayers)
    publish(new NumberOfPlayersChanged)
    gameModel.players.length
  }

  def nextRound(): Integer = {
    updateMrXVisibility()
    gameModel.increaseRound()
    gameModel.updateTotalRound()
    if (!checkIfPlayerIsAbleToMove()) {
      gameModel = gameModel.addStuckPlayer()
      if (gameModel.stuckPlayers.size == gameModel.players.size - 1) {
        winGame(gameModel.getCurrentPlayer)
      } else {
        nextRound()
      }
    }
    gameModel.round
  }

  def previousRound(): Integer = {
    updateMrXVisibility()
    gameModel.decreaseRound()
    gameModel.updateTotalRound()
    gameModel.round
  }

  def checkMrXVisibility(): Boolean = {
    gameModel.MRX_VISIBLE_ROUNDS.contains(gameModel.totalRound)
  }

  private def checkIfPlayerIsAbleToMove(): Boolean = {
    gameModel.getCurrentPlayer.station.stationType match {
      case StationType.Taxi =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0
      case model.StationType.Bus =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0 || gameModel.getCurrentPlayer.tickets.busTickets > 0
      case model.StationType.Underground =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0 || gameModel.getCurrentPlayer.tickets.busTickets > 0 || gameModel.getCurrentPlayer.tickets.undergroundTickets > 0
    }
  }

  private def checkDetectiveWin(): Boolean = {
    for (dt <- gameModel.getDetectives) {
      if (dt.station.number == gameModel.getMrX.station.number) {
        return true
      }
    }
    false
  }

  private def checkMrXWin(): Boolean = {
    gameModel.round == gameModel.WINNING_ROUND * gameModel.players.length
  }

  def move(newPosition: Int, ticketType: TicketType): Station = {
    if(validateMove(newPosition, ticketType)) {
      val newStation = undoManager.doStep(new MoveCommand(gameModel.getCurrentPlayer.station.number, newPosition, ticketType))
      publish(new PlayerMoved)

      if (checkDetectiveWin()) {
        winGame(gameModel.getLastPlayer)
      }
      if (checkMrXWin()) {
        winGame(gameModel.getLastPlayer)
      }
      newStation
    } else {
      gameModel.getCurrentPlayer.station
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
    val mrX = getMrX
    mrX.isVisible = checkMrXVisibility()
    if (mrX.isVisible) {
      mrX.lastSeen = gameModel.players.head.station.number.toString
    }
    mrX.isVisible
  }

  def startGame(): Boolean = {
    publish(new StartGame)
    true
  }

  def winGame(winningPlayer: DetectiveInterface): Boolean = {
    gameModel.winGame(winningPlayer)
    publish(new PlayerWin)
    gameModel.win
  }

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    if (!isTargetStationInBounds(newPosition)) return false
    if (gameModel.getCurrentPlayer.station.number == newPosition) return false
    if (!isMeanOfTransportValid(newPosition, ticketType)) return false
    if (!isTargetStationEmpty(newPosition)) return false
    true
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean = {
    newPosition < gameModel.stations.size && newPosition > 0
  }

  private def isMeanOfTransportValid(newPosition: Integer, ticketType: TicketType): Boolean = {
    val player = gameModel.getCurrentPlayer
    ticketType match {
      case TicketType.Taxi =>
        isTransportMoveValid(newPosition)(player.tickets.taxiTickets, player.station.neighbourTaxis)
      case TicketType.Bus =>
        if (player.station.stationType == StationType.Taxi) return false
        isTransportMoveValid(newPosition)(player.tickets.busTickets, player.station.neighbourBuses)
      case TicketType.Underground =>
        if (player.station.stationType != StationType.Underground) return false
        isTransportMoveValid(newPosition)(player.tickets.undergroundTickets, player.station.neighbourUndergrounds)
      case _ =>
        if (!player.equals(gameModel.players.head)) return false
        isBlackMoveValid(newPosition)
    }
  }

  private def isTargetStationEmpty(newPosition: Integer): Boolean = {
    for ((p, index) <- gameModel.players.zipWithIndex) {
      breakable {
        if (index == 0 && !gameModel.getCurrentPlayer.equals(gameModel.getMrX)) break
        if (p.station.number == newPosition) return false
      }
    }
    true
  }

  private def isTransportMoveValid(newPosition: Int)(tickets: Int, neighbours: Set[Station]): Boolean = {
    if (tickets <= 0) return false
    neighbours.contains(gameModel.stations(newPosition))
  }

  private def isBlackMoveValid(newPosition: Int): Boolean = {
    if (gameModel.getCurrentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets <= 0) return false
    gameModel.getCurrentPlayer.station.neighbourTaxis.contains(gameModel.stations(newPosition)) ||
      gameModel.getCurrentPlayer.station.neighbourBuses.contains(gameModel.stations(newPosition)) ||
      gameModel.getCurrentPlayer.station.neighbourUndergrounds.contains(gameModel.stations(newPosition))
  }

  // Getters and Setters
  def getCurrentPlayer: DetectiveInterface = {
    gameModel.getCurrentPlayer
  }

  def getMrX: MrXInterface = {
    gameModel.getMrX
  }

  def getPlayersList(): List[DetectiveInterface] = {
    gameModel.players
  }

  def getStations(): List[Station] = {
    gameModel.stations
  }

  def getTotalRound(): Integer = {
    gameModel.totalRound
  }

  def getWin(): Boolean = {
    gameModel.win
  }

  def getGameRunning(): Boolean = {
    gameModel.gameRunning
  }

  def getWinningPlayer(): DetectiveInterface = {
    gameModel.winningPlayer
  }

  def setPlayerName(inputName: String, index: Int): Boolean = {
    var returnValue: Boolean = false
    returnValue = gameModel.players(index).setPlayerName(inputName)
    publish(new PlayerNameChanged)
    returnValue
  }

  def setPlayerColor(newColor: String, index: Int): Color = {
    val returnValue = gameModel.players(index).setPlayerColor(newColor)
    publish(new PlayerColorChanged)
    returnValue
  }

  def updateLobby(): Boolean = {
    publish(new LobbyChange)
    true
  }
}
