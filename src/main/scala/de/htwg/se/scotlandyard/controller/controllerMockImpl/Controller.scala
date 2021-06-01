package de.htwg.se.scotlandyard.controller.controllerMockImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType}
import de.htwg.se.scotlandyard.model.players.{Detective, MrX, Player}
import de.htwg.se.scotlandyard.model.TicketType.TicketType

import java.awt.Color
import scala.swing.Publisher

class Controller extends ControllerInterface with Publisher {
  override def initializeStations(stationsSource: String): Boolean = true

  override def initialize(nPlayer: Int): Unit = Unit

  override def load(): Option[GameModel] = Some(GameModel())

  override def save(): Boolean = true

  override def startGame(): Boolean = true

  override def winGame(winningPlayer: Player): Boolean = false

  override def getCurrentPlayer: Player = Detective()

  override def getMrX: MrX = MrX()

  override def getDetectives(): Vector[Detective] = Vector(Detective(), Detective())

  override def getStations(): Vector[Station] = Vector(Station(0, StationType.Taxi))

  override def getTotalRound(): Int = 3

  override def getWin(): Boolean = true

  override def getGameRunning(): Boolean = false

  override def getStationOfPlayer(player: Player): Station = Station()

  override def getWinningPlayer(): Player = Detective()

  override def setPlayerName(inputName: String, index: Int): Boolean = {
    if(inputName.length < 3) return false
    true
  }

  override def setPlayerColor(newColor: String, index: Int): Color = {
    Color.BLACK
  }

  override def updateLobby(): Boolean = true

  override def move(newPosition: Int, ticketType: TicketType): GameModel = GameModel()

  override def undoMove(): GameModel = GameModel()

  override def redoMove(): GameModel = GameModel()
}