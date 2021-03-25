package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.GameModel.{WINNING_ROUND, players, stations}
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import org.scalatest._

import java.awt.Color

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      val gameInitializer = new GameInitializer(new TuiMap)
      gameInitializer.initialize(5)

      val controller = new Controller(gameInitializer, new FileIO(gameInitializer))

      "should validateAndMove" in {
        gameInitializer.initialize(5)
        GameModel.getMrX.station = GameModel.stations(3)

        controller.move(2, TicketType.Taxi).number should be(3)

        GameModel.getMrX.station = GameModel.stations(10)
        controller.move(2, TicketType.Taxi).number should be(2)

        controller.undoValidateAndMove().number should be(10)
        controller.redoValidateAndMove().number should be(2)

        GameModel.getCurrentPlayer.station = GameModel.stations(1)
        GameModel.players.head.station = GameModel.stations(3)
        controller.move(2, TicketType.Taxi).number should be(1)
        controller.move(46, TicketType.Underground).number should be(46)
        controller.undoValidateAndMove().number should be(1)
        controller.redoValidateAndMove().number should be(46)
      }
      "should return 3 from getPlayerList method" in {
        controller.gameInitializer.initialize()
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
        controller.getWinningPlayer() should be(GameModel.winningPlayer)
      }
      "and getTotalRound" in {
        controller.getTotalRound() should be(GameModel.totalRound)
      }
      "and getMrX" in {
        controller.getMrX should be(GameModel.getMrX)
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
      "and move() should return" in {
        GameModel.getCurrentPlayer.station = GameModel.stations(153)
        GameModel.players.head.station = GameModel.stations(180)
        controller.move(166, TicketType.Taxi).number should be(166)
        controller.move(165, TicketType.Bus).number should not be 165
        controller.move(1, TicketType.Underground).number should not be 1

        GameModel.players.head.station = GameModel.stations(55)
        GameModel.getCurrentPlayer.station = GameModel.stations(89)
        controller.move(71, TicketType.Taxi).number should be(71)

        GameModel.getCurrentPlayer.station = GameModel.stations(185)
        controller.move(128, TicketType.Underground).number should be(128)

        GameModel.getCurrentPlayer.station = GameModel.stations(89)
        controller.move(105, TicketType.Bus).number should be(105)

        GameModel.getCurrentPlayer.station = GameModel.stations(91)
        controller.move(90, TicketType.Taxi).number should be(90)
      }
      "and move() should fail because missing tickets" in {
        GameModel.getCurrentPlayer.tickets.taxiTickets = 0;
        GameModel.getCurrentPlayer.tickets.busTickets= 0;
        GameModel.getCurrentPlayer.tickets.undergroundTickets = 0;

        GameModel.getCurrentPlayer.station = GameModel.stations(67)
        controller.move(79, TicketType.Underground).number should not be 79
        controller.move(102, TicketType.Bus).number should not be 102
        controller.move(66, TicketType.Taxi).number should not be 66
      }
      "and move should win the game for detectives" in {
        gameInitializer.initialize()
        GameModel.getMrX.station = GameModel.stations(1)
        GameModel.players(2).station = GameModel.stations(8)
        GameModel.round = 3
        controller.move(1, TicketType.Taxi).number should be(1)
        GameModel.win should be (true)
      }
      "and move in last round should win the game for MrX" in {
        //TODO: Winning doesnt work correct!
        gameInitializer.initialize()
        GameModel.totalRound = 24
        GameModel.round = GameModel.players.length * GameModel.WINNING_ROUND - 1
        GameModel.getMrX.station = GameModel.stations(1)
        GameModel.players(1).station = GameModel.stations(8) // should not be 1 ?
        controller.move(18, TicketType.Taxi).number should be(18)
        GameModel.win should be (true)
      }
      "and MrX should also be hidden" in {
        gameInitializer.initialize(2) should be (true)
        GameModel.totalRound = 1
        controller.checkMrXVisibility() shouldBe (false)
        GameModel.totalRound = 3
        controller.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 8
        controller.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 13
        controller.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 18
        controller.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 24
        controller.checkMrXVisibility() shouldBe (true)

        GameModel.getMrX.lastSeen shouldBe ("never")
      }
      "and MrX should win in round 24" in {
        GameModel.round = WINNING_ROUND * players.length
        controller invokePrivate PrivateMethod[Boolean](Symbol("checkMrXWin"))() should be(true)
      }
      "and winGame" in{
        controller.winGame(new Detective()) should be(true)
      }
    }
  }
}
