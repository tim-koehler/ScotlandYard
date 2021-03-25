package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.{Station, TicketType}
import de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import org.scalatest._

import java.awt.Color

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      val gameInitializer = new GameInitializer(new TuiMap)
      var gameModel = gameInitializer.initialize(5)

      val controller = new Controller(gameInitializer, new FileIO(gameInitializer), gameModel)

      "should validateAndMove" in {
        gameInitializer.initialize(5)
        gameModel.getMrX.station = gameModel.stations(3)

        controller.move(2, TicketType.Taxi).number should be(3)

        gameModel.getMrX.station = gameModel.stations(10)
        controller.move(2, TicketType.Taxi).number should be(2)

        controller.undoValidateAndMove().number should be(10)
        controller.redoValidateAndMove().number should be(2)

        gameModel.getCurrentPlayer.station = gameModel.stations(1)
        gameModel.players.head.station = gameModel.stations(3)
        controller.move(2, TicketType.Taxi).number should be(1)
        controller.move(46, TicketType.Underground).number should be(46)
        controller.undoValidateAndMove().number should be(1)
        controller.redoValidateAndMove().number should be(46)
      }
      "should return 3 from getPlayerList method" in {
        controller.initialize()
        controller.getPlayersList().length shouldBe (3)
      }
      "should return true from setPlayerNames when index is correct" in {
        controller.setPlayerName("Tim", 1) shouldBe true
      }
      "should return 2 from nextRound Method" in {
        controller.nextRound() shouldBe(2)
        gameModel.totalRound shouldBe(1)
      }
      "should return the correct player from setPlayerNumber" in {
        controller.initialize() shouldBe (3)
      }
      "and updateMrXVisibility" in {
        controller.updateMrXVisibility() should be(false)
      }
      "and setPlayerColor" in {
        controller.setPlayerColor("#ffffff", 1) should be (Color.BLUE)
      }
      "and previous round" in {
        controller.previousRound() should not be(-1)
      }
      "and getCurrentPlayer" in{
        gameInitializer.initialize(3)
        controller.nextRound()
        controller.getCurrentPlayer.name should be("Dt1")
      }
      "and getStations" in {
        controller.getStations() should not be(Set[Station]())
      }
      "and getWin" in{
        controller.getWin() should be (false)
      }
      "and getGameRunning" in {
        controller.getGameRunning() should be (true)
      }
      "and getWinningPlayer" in {
        controller.getWinningPlayer() should be(gameModel.winningPlayer)
      }
      "and getTotalRound" in {
        controller.getTotalRound() should be(gameModel.totalRound)
      }
      "and getMrX" in {
        controller.getMrX should be(gameModel.getMrX)
      }
      "and startGame" in {
        controller.startGame() should be(true)
      }
      "should load and save" in {
        controller.load() should be(true)
        controller.save() should be(true)
      }
      "and move() should return" in {
        gameModel.getCurrentPlayer.station = gameModel.stations(153)
        gameModel.players.head.station = gameModel.stations(180)
        controller.move(166, TicketType.Taxi).number should be(166)
        controller.move(165, TicketType.Bus).number should not be 165
        controller.move(1, TicketType.Underground).number should not be 1

        gameModel.players.head.station = gameModel.stations(55)
        gameModel.getCurrentPlayer.station = gameModel.stations(89)
        controller.move(71, TicketType.Taxi).number should be(71)

        gameModel.getCurrentPlayer.station = gameModel.stations(185)
        controller.move(128, TicketType.Underground).number should be(128)

        gameModel.getCurrentPlayer.station = gameModel.stations(89)
        controller.move(105, TicketType.Bus).number should be(105)

        gameModel.getCurrentPlayer.station = gameModel.stations(91)
        controller.move(90, TicketType.Taxi).number should be(90)
      }
      "and move() should fail because missing tickets" in {
        gameModel.getCurrentPlayer.tickets.taxiTickets = 0;
        gameModel.getCurrentPlayer.tickets.busTickets= 0;
        gameModel.getCurrentPlayer.tickets.undergroundTickets = 0;

        gameModel.getCurrentPlayer.station = gameModel.stations(67)
        controller.move(79, TicketType.Underground).number should not be 79
        controller.move(102, TicketType.Bus).number should not be 102
        controller.move(66, TicketType.Taxi).number should not be 66
      }
      "and move should win the game for detectives" in {
        controller.initialize()
        gameModel = gameModel.copy(round = 3)
        gameModel.getMrX.station = gameModel.stations(1)
        gameModel.players(2).station = gameModel.stations(8)
        controller.move(1, TicketType.Taxi).number should be(1)
        gameModel.win should be (true)
      }
      "and move in last round should win the game for MrX" in {
        //TODO: Winning doesnt work correct!
        controller.initialize()
        gameModel = gameModel.copy(totalRound = 24, round = gameModel.players.length * gameModel.WINNING_ROUND - 1)
        gameModel.getMrX.station = gameModel.stations(1)
        gameModel.players(1).station = gameModel.stations(8) // should not be 1 ?
        controller.move(18, TicketType.Taxi).number should be(18)
        gameModel.win should be (true)
      }
      "and MrX should also be hidden" in {
        controller.initialize(2) should be (2)
        controller.checkMrXVisibility() shouldBe (false)
        gameModel = gameModel.copy(totalRound = 3)
        controller.checkMrXVisibility() shouldBe (true)
        gameModel = gameModel.copy(totalRound = 8)
        controller.checkMrXVisibility() shouldBe (true)
        gameModel = gameModel.copy(totalRound = 13)
        controller.checkMrXVisibility() shouldBe (true)
        gameModel = gameModel.copy(totalRound = 18)
        controller.checkMrXVisibility() shouldBe (true)
        gameModel = gameModel.copy(totalRound = 24)
        controller.checkMrXVisibility() shouldBe (true)

        gameModel.getMrX.lastSeen shouldBe ("never")
      }
      "and MrX should win in round 24" in {
        gameModel = gameModel.copy(round = gameModel.WINNING_ROUND * gameModel.players.length)
        controller invokePrivate PrivateMethod[Boolean](Symbol("checkMrXWin"))() should be(true)
      }

      "should return true with a black move" in {
        val gameInitializer = new GameInitializer(new TuiMap)
        gameInitializer.initialize(5)
        gameModel.getMrX.station = gameModel.stations(34)
        controller.validateMove(22, TicketType.Black) should be (true)
      }
      "and winGame" in{
        controller.winGame(new Detective()) should be(true)
      }
    }
  }
}
