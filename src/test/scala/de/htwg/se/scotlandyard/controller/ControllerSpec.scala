package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.{Detective, MrX}
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.{StationType, TicketType}
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      val controller = new Controller()
      controller.fileIO = new FileIO()
      controller.gameInitializer = new GameInitializer()
      controller.gameInitializer.initialize(3)

      "should return 3 from getPlayerList method" in {
        controller.gameInitializer.initialize(3)
        controller.getPlayersList().length shouldBe (3)
      }
      "should return true from setPlayerNames when index is correct" in {
        controller.setPlayerName("Tim", 1) shouldBe true
      }
      "should 2 from nextRound Method" in {
        GameMaster.round = 1
        controller.nextRound() shouldBe(2)
      }
      "should return the correct player from setPlayerNumber" in {
        controller.initPlayers(3) shouldBe (3)
      }
      "should validateAndMove" in {
        controller.undoValidateAndMove().sType should be(StationType.Taxi)
        controller.redoValidateAndMove().sType should be(StationType.Taxi)

        GameMaster.getCurrentPlayer().station =  GameMaster.stations(1)
        GameMaster.players.head.station = GameMaster.stations(3)

        controller.doMove(2, TicketType.Taxi).sType should be(GameMaster.stations(2).sType)
        controller.undoValidateAndMove().sType should be(StationType.Underground)
        controller.redoValidateAndMove().sType should be(StationType.Taxi)

        for (p <- GameMaster.players){
          p.station = GameMaster.stations(1)
        }
        GameMaster.getCurrentPlayer().station = GameMaster.stations(2)

        controller.doMove(3, TicketType.Bus).sType should be(StationType.Bus)
        controller.undoValidateAndMove().sType should be(StationType.Taxi)

        controller.doMove(3, TicketType.Underground).sType should be(StationType.Bus)
        controller.undoValidateAndMove().sType should be(StationType.Taxi)
      }
      "and updateMrXVisibility" in {
        controller.updateMrXVisibility() should be(false)
      }
      "and previous round" in {
        controller.previousRound() should not be(-1)
      }
      "and getStations" in {
        controller.getStations() should not be(Set[Station]())
      }
      "and setWin" in {
        controller.setWinning(true) should be(true)
        controller.getWin() should be(true)
      }
      "and winGame" in{
        controller.winGame() should be(true)
      }
      "and getWin" in {
        controller.setWinning(true)
        controller.getWin() should be(true)
      }
      "and getWinningPlayer" in {
        controller.getWinningPlayer() should be(GameMaster.winningPlayer)
      }
      "and getTotalRound" in {
        controller.getTotalRound() should be(GameMaster.totalRound)
      }
      "and getMrX" in {
        controller.getMrX() should be(GameMaster.getMrX())
      }
      "and startGame" in {
        controller.startGame() should be(true)
      }
      "and validateMove" in {
        controller.validateMove(10, TicketType.Taxi) should be(GameMaster.validateMove(10, TicketType.Taxi))
      }
      "should load and save" in {
        controller.load() should be(true)
        controller.save() should be(true)
      }
    }
  }
}
