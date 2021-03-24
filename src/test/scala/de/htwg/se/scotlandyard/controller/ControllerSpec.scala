package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.GameModel.{WINNING_ROUND, players}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
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
        gameInitializer.initialize(5)

        println(GameModel.stations)

        GameModel.getMrX().station = GameModel.stations(3)

        controller.move(2, TicketType.Taxi).stationType should be(StationType.Taxi)

        controller.undoValidateAndMove().stationType should be(StationType.Bus)
        controller.redoValidateAndMove().stationType should be(StationType.Taxi)

        GameModel.getCurrentPlayer().station =  GameModel.stations(1)
        GameModel.players.head.station = GameModel.stations(3)

        controller.move(2, TicketType.Taxi).stationType should be(GameModel.stations(2).stationType)
        controller.undoValidateAndMove().stationType should be(StationType.Underground)
        controller.redoValidateAndMove().stationType should be(StationType.Taxi)

        for (p <- GameModel.players){
          p.station = GameModel.stations(1)
        }
        GameModel.getCurrentPlayer().station = GameModel.stations(2)

        controller.move(3, TicketType.Bus).stationType should be(StationType.Bus)
        controller.undoValidateAndMove().stationType should be(StationType.Taxi)

        controller.move(3, TicketType.Underground).stationType should be(StationType.Bus)
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
        GameModel.round = 1
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
      "and winGame" in{
        //controller.winGame() should be(true)
      }
      "and getWinningPlayer" in {
        controller.getWinningPlayer() should be(GameModel.winningPlayer)
      }
      "and getTotalRound" in {
        controller.getTotalRound() should be(GameModel.totalRound)
      }
      "and getMrX" in {
        controller.getMrX() should be(GameModel.getMrX())
      }
      "and startGame" in {
        controller.startGame() should be(true)
      }
      "should load and save" in {
        controller.load() should be(true)
        controller.save() should be(true)
      }
      "and the next round should be 2" in {
        GameModel.round = 1
        controller.nextRound() shouldBe(2)
        GameModel.totalRound shouldBe(1)
      }
      "and target Station is in Bounds" in {
        controller invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1) should be(true)
        controller invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1000) should be(false)
      }
      "and move() should return" in {
        GameModel.getCurrentPlayer().station = GameModel.stations(153)
        GameModel.players(0).station = GameModel.stations(180)

        controller.move(166, TicketType.Taxi).number should be(166)

        controller.move(165, TicketType.Bus).number should not be 165

        controller.move(1, TicketType.Underground).number should not be 1

        GameModel.players(0).station = GameModel.stations(55)
        GameModel.getCurrentPlayer().station = GameModel.stations(89)

        controller.move(71, TicketType.Taxi).number should be(71)

        controller.move(128, TicketType.Underground).number should be(128)

        GameModel.getCurrentPlayer().station = GameModel.stations(89)
        controller.move(105, TicketType.Bus).number should be(105)
        controller.move(90, TicketType.Taxi) should not be 90
      }
      "and move() should return new Station" in {
        GameModel.getCurrentPlayer().tickets.taxiTickets = 0;
        GameModel.getCurrentPlayer().tickets.busTickets= 0;
        GameModel.getCurrentPlayer().tickets.undergroundTickets = 0;

        controller.move(128, TicketType.Underground).number should not be 128
        controller.move(55, TicketType.Bus).number should not be 55
        controller.move(88, TicketType.Taxi).number should not be 88
      }
      "mrx should win in round 24" in {
        GameModel.round = WINNING_ROUND * players.length
        controller invokePrivate PrivateMethod[Boolean](Symbol("checkMrXWin"))() should be(true)
      }
    }
  }
}
