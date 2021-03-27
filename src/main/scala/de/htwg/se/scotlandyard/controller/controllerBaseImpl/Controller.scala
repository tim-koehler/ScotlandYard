package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.controller.{ControllerInterface, LobbyChange, NumberOfPlayersChanged, PlayerColorChanged, PlayerMoved, PlayerNameChanged, PlayerWin, StartGame}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.controller.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.GameInitializerInterface

import java.awt.Color
import scala.swing.Publisher
import scala.util.control.Breaks.{break, breakable}

class Controller @Inject()(override val gameInitializer: GameInitializerInterface,
                           override val fileIO: FileIOInterface) extends ControllerInterface with Publisher {

  private var gameModel: GameModel = initialize(3)
  private val undoManager = new UndoManager()

  def initialize(nPlayers: Int = 3): GameModel = {
    gameModel = gameInitializer.initialize(nPlayers)
    publish(new NumberOfPlayersChanged)
    gameModel
  }

  def load(): Boolean = {
    gameModel = fileIO.load()
    true
  }

  def save(): Boolean = {
    fileIO.save(gameModel, gameModel.getMrX(gameModel.players))
    true
  }

  private def checkDetectiveWin(): Boolean = {
    for (dt <- gameModel.getDetectives(gameModel.players)) {
      if (dt.station.number == gameModel.getMrX(gameModel.players).station.number) {
        return true
      }
    }
    false
  }

  private def checkMrXWin(): Boolean = {
    gameModel.round == gameModel.WINNING_ROUND * gameModel.players.length
  }

  def move(newPosition: Int, ticketType: TicketType): GameModel = {
    if(!validateMove(newPosition, ticketType)) {
      return this.gameModel
    }
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    this.gameModel = undoManager.doStep(new MoveCommand(currentPlayer.station.number, newPosition, ticketType), this.gameModel)
    publish(new PlayerMoved)

    if (gameModel.allPlayerStuck) winGame(gameModel.getMrX(gameModel.players))
    if (checkDetectiveWin()) winGame(gameModel.getPreviousPlayer(gameModel.players, gameModel.round))
    if (checkMrXWin()) winGame(gameModel.getMrX(gameModel.players))
    gameModel
  }

  def winGame(winningPlayer: DetectiveInterface): Boolean = {
    this.gameModel = gameModel.winGame(gameModel, winningPlayer)
    publish(new PlayerWin)
    this.gameModel.win
  }

  def undoMove(): GameModel = {
    this.gameModel = undoManager.undoStep(this.gameModel)
    publish(new PlayerMoved)
    this.gameModel
  }

  def redoMove(): GameModel = {
    this.gameModel = undoManager.redoStep(this.gameModel)
    publish(new PlayerMoved)
    this.gameModel
  }

  def startGame(): Boolean = {
    publish(new StartGame)
    true
  }

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    if (!isTargetStationInBounds(newPosition)) return false
    if (currentPlayer.station.number == newPosition) return false
    if (!isMeanOfTransportValid(currentPlayer, newPosition, ticketType)) return false
    if (!isTargetStationEmpty(currentPlayer, newPosition)) return false
    true
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean = {
    newPosition < gameModel.stations.size && newPosition > 0
  }

  private def isMeanOfTransportValid(player: DetectiveInterface,newPosition: Integer, ticketType: TicketType): Boolean = {
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
        isBlackMoveValid(player, newPosition)
    }
  }

  private def isTargetStationEmpty(player: DetectiveInterface, newPosition: Integer): Boolean = {
    for ((p, index) <- gameModel.players.zipWithIndex) {
      breakable {
        if (index == 0 && !player.equals(gameModel.getMrX(gameModel.players))) break
        if (p.station.number == newPosition) return false
      }
    }
    true
  }

  private def isTransportMoveValid(newPosition: Int)(tickets: Int, neighbours: Set[Station]): Boolean = {
    if (tickets <= 0) return false
    neighbours.contains(gameModel.stations(newPosition))
  }

  private def isBlackMoveValid(currentPlayer: DetectiveInterface,newPosition: Int): Boolean = {

    if (currentPlayer.asInstanceOf[MrXInterface].tickets.blackTickets <= 0) return false
    currentPlayer.station.neighbourTaxis.contains(gameModel.stations(newPosition)) ||
      currentPlayer.station.neighbourBuses.contains(gameModel.stations(newPosition)) ||
      currentPlayer.station.neighbourUndergrounds.contains(gameModel.stations(newPosition))
  }

  // Getters and Setters
  def getCurrentPlayer: DetectiveInterface = {
    gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
  }

  def getMrX: MrXInterface = {
    gameModel.getMrX(gameModel.players)
  }

  def getPlayersList(): Vector[DetectiveInterface] = {
    gameModel.players
  }

  def getStations(): Vector[Station] = {
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
