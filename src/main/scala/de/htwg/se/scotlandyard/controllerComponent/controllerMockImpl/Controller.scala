package de.htwg.se.scotlandyard.controllerComponent.controllerMockImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.model.{Station, StationType}
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIOComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.{Detective, MrX}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import org.scalactic.anyvals.PosInt

import scala.swing.Publisher

class Controller @Inject()(override var gameInitializer: GameInitializerInterface,
                           override var fileIO: FileIOInterface) extends ControllerInterface with Publisher {

  override def load(): Boolean = true

  override def save(): Boolean = true

  override def initPlayers(nPlayer: Int): Integer = nPlayer

  override def nextRound(): Integer = 2

  override def previousRound(): Integer = 1

  override def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    if(newPosition > 299) return false
    true
  }

  override def doMove(newPosition: Int, ticketType: TicketType): Station = new Station(0, StationType.Taxi)

  override def undoValidateAndMove(): Station = new Station(0, StationType.Taxi)

  override def redoValidateAndMove(): Station = new Station(0, StationType.Taxi)

  override def updateMrXVisibility(): Boolean = true

  override def startGame(): Boolean = true

  override def winGame(): Boolean = true

  override def getCurrentPlayer(): DetectiveInterface = new Detective()

  override def getMrX(): MrXInterface = new MrX()

  override def getPlayersList(): List[DetectiveInterface] = List(new MrX, new Detective, new Detective)

  override def getStations(): List[Station] = List(new Station(0, StationType.Taxi))

  override def getTotalRound(): Integer = 3

  override def getWin(): Boolean = true

  override def getGameRunning(): Boolean = false

  override def getWinningPlayer(): DetectiveInterface = new Detective()

  override def setPlayerName(inputName: String, index: Int): Boolean = {
    if(inputName.length < 3) return false
    true
  }

  override def setPlayerColor(newColor: String, index: Int): Boolean = {
    true
  }

  override def setWinning(win: Boolean): Boolean = win

  override def updateLobby(): Boolean = true

}