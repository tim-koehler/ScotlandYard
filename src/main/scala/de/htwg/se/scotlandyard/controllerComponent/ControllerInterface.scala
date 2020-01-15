package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.util.TicketType.TicketType

import scala.swing.Publisher

trait ControllerInterface extends Publisher {

  def load(): Unit
  def save(): Unit
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