package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}

import java.awt.Color
import scala.concurrent.Future
import scala.swing.Publisher

trait ControllerInterface extends Publisher {
  def initializeStations(stationsSource: String): Boolean
  def initialize(nPlayer: Int = 3): Unit
  def load(): Option[GameModel]
  def save(): Boolean
  def move(newPosition: Int, ticketType: TicketType): GameModel
  def undoMove(): GameModel
  def redoMove(): GameModel
  def startGame(): Boolean
  def winGame(winningPlayer: Player): Boolean
  def getGameRunning(): Boolean
  def getCurrentPlayer: Player
  def getMrX: MrX
  def getDetectives: Vector[Detective]
  def getStations(): Vector[Station]
  def getTotalRound(): Int
  def getWin(): Boolean
  def getWinningPlayer(): Player
  def getStationOfPlayer(player: Player): Station
  def setPlayerName(inputName: String, index: Int): Boolean
  def setPlayerColor(newColor: String, index: Int): Color
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
class GameLoaded extends Event
class GameNotLoaded extends Event