package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.{GameMaster, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      val gameInitializer = new GameInitializer()
      gameInitializer.initialize(5)

      val controller = new Controller(gameInitializer, new FileIO(gameInitializer))

      "should validateAndMove" in {
        GameMaster.initialize(5)

        controller.validateMove(45, TicketType.Underground) should be(false)

        GameMaster.getCurrentPlayer().station = GameMaster.stations(1)
        GameMaster.getMrX().station = GameMaster.stations(3)

        controller.doMove(2, TicketType.Taxi).stationType should be(StationType.Taxi)

        controller.undoValidateAndMove().stationType should be(StationType.Bus)
        controller.redoValidateAndMove().stationType should be(StationType.Taxi)

        GameMaster.getCurrentPlayer().station =  GameMaster.stations(1)
        GameMaster.players.head.station = GameMaster.stations(3)

        controller.doMove(2, TicketType.Taxi).stationType should be(GameMaster.stations(2).stationType)
        controller.undoValidateAndMove().stationType should be(StationType.Underground)
        controller.redoValidateAndMove().stationType should be(StationType.Taxi)

        for (p <- GameMaster.players){
          p.station = GameMaster.stations(1)
        }
        GameMaster.getCurrentPlayer().station = GameMaster.stations(2)

        controller.doMove(3, TicketType.Bus).stationType should be(StationType.Bus)
        controller.undoValidateAndMove().stationType should be(StationType.Taxi)

        controller.doMove(3, TicketType.Underground).stationType should be(StationType.Bus)
        controller.undoValidateAndMove().stationType should be(StationType.Taxi)
      }
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
        GameMaster.win = true
        controller.setWinning(false) should be(true)
        controller.getWin() should be(false)
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
      "should load and save" in {
        controller.load() should be(true)
        controller.save() should be(true)
      }
    }
  }
}
