package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.util.TicketType.TicketType

import scala.swing.Publisher

trait ControllerInterface extends Publisher {

  var gameInitializer: GameInitializerInterface
  var fileIO: FileIOInterface

  def load(): Boolean
  def save(): Boolean
  def initPlayers(nPlayer: Int): Integer
  def nextRound(): Integer
  def previousRound(): Integer
  def validateMove(newPosition: Int, ticketType: TicketType): Boolean
  def doMove(newPosition: Int, ticketType: TicketType): Station
  def undoValidateAndMove(): Station
  def redoValidateAndMove(): Station
  def updateMrXVisibility(): Boolean
  def startGame(): Boolean
  def winGame(): Boolean
  def getGameRunning(): Boolean
  def getCurrentPlayer(): DetectiveInterface
  def getMrX(): MrXInterface
  def getPlayersList(): List[DetectiveInterface]
  def getStations(): List[Station]
  def getTotalRound(): Integer
  def getWin(): Boolean
  def getWinningPlayer(): DetectiveInterface
  def setPlayerName(inputName: String, index: Int): Boolean
  def setWinning(win: Boolean): Boolean
}

import scala.swing.event.Event

class PlayerNameChanged extends Event
class NumberOfPlayersChanged extends Event
class PlayerMoved extends Event 
class PlayerWin extends Event
class StartGame extends Event