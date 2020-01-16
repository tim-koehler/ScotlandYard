package de.htwg.se.scotlandyard.controllerComponent.controllerMockImpl

import de.htwg.se.scotlandyard.controllerComponent.ControllerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIoComponent.FileIOInterface
import de.htwg.se.scotlandyard.model.fileIoComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.{Detective, MrX}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class Controller extends ControllerInterface{
  override var gameInitializer: GameInitializerInterface = new GameInitializer()
  override var fileIO: FileIOInterface = new FileIO()

  override def load(): Boolean = true

  override def save(): Boolean = true

  override def initPlayers(nPlayer: Int): Integer = nPlayer

  override def nextRound(): Integer = 2

  override def previousRound(): Integer = 1

  override def validateMove(newPosition: Int, ticketType: TicketType): Boolean = true

  override def doMove(newPosition: Int, ticketType: TicketType): Station = StationFactory.createZeroIndexStation()

  override def undoValidateAndMove(): Station = StationFactory.createZeroIndexStation()

  override def redoValidateAndMove(): Station = StationFactory.createZeroIndexStation()

  override def updateMrXVisibility(): Boolean = true

  override def startGame(): Boolean = true

  override def winGame(): Boolean = true

  override def getCurrentPlayer(): DetectiveInterface = new Detective()

  override def getMrX(): MrXInterface = new MrX()

  override def getPlayersList(): List[DetectiveInterface] = List(new Detective())

  override def getStations(): List[Station] = List(StationFactory.createZeroIndexStation())

  override def getTotalRound(): Integer = 3

  override def getWin(): Boolean = true

  override def getWinningPlayer(): DetectiveInterface = new Detective()

  override def setPlayerName(inputName: String, index: Int): Boolean = true

  override def setWinning(win: Boolean): Boolean = win
}