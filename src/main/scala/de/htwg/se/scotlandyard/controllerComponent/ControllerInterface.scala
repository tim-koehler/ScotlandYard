package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.Station
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface

import scala.swing.Publisher

trait ControllerInterface extends Publisher {

  var gameInitializer: GameInitializerInterface
  var fileIO: FileIOInterface

  def load(): Boolean
  def save(): Boolean
  def initPlayers(nPlayer: Int): Integer
  def nextRound(): Integer
  def previousRound(): Integer
  def move(newPosition: Int, ticketType: TicketType): Station
  def undoValidateAndMove(): Station
  def redoValidateAndMove(): Station
  def updateMrXVisibility(): Boolean
  def startGame(): Boolean
  def winGame(winningPlayer: DetectiveInterface): Boolean
  def getGameRunning(): Boolean
  def getCurrentPlayer: DetectiveInterface
  def getMrX: MrXInterface
  def getPlayersList(): List[DetectiveInterface]
  def getStations(): List[Station]
  def getTotalRound(): Integer
  def getWin(): Boolean
  def getWinningPlayer(): DetectiveInterface
  def setPlayerName(inputName: String, index: Int): Boolean
  def setPlayerColor(newColor: String, index: Int): Boolean
  def updateLobby(): Boolean
}

import scala.swing.event.Event

class PlayerColorChanged extends Event
class PlayerNameChanged extends Event
class NumberOfPlayersChanged extends Event
class PlayerMoved extends Event
class PlayerWin extends Event
class StartGame extends Event
class LobbyChange extends Event