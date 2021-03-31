package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.{GameModel, TicketType}
import de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest._

import java.awt.Color
import scala.io.Source

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester with BeforeAndAfterEach {

  val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
  val gameInitializer = new GameInitializer()
  val controller = new Controller(gameInitializer, new FileIO(gameInitializer))
  controller.initializeStations(stationsSource)
  var gameModel = GameModel()

  override def beforeEach(): Unit = {
    gameModel = controller.initialize(3)
  }

  "Controller" when {
    "initialize" should {
      "return a gameModel" in {
        controller.initialize(3).players.length should be(3)
      }
    }
    "load" should {
      "return true" in {
        controller.load().round should be(1)
        controller.load().totalRound should be(1)
      }
    }

    "save" should {
      "return true" in {
        controller.save() should be(true)
      }
    }

    "move" should {
      "validate mrx taxi move" in {
        controller.move(9, TicketType.Taxi).players(0).station.number should be (9)
      }
      "validate mrx bus move" in {
        controller.move(58, TicketType.Bus).players(0).station.number should be (58)
      }
      "validate mrx underground move" in {
        controller.move(46, TicketType.Underground).players(0).station.number should be (46)
      }
      "validate mrx black (taxi) move" in {
        controller.move(9, TicketType.Black).players(0).station.number should be (9)
      }
      "validate mrx black (bus) move" in {
        controller.move(58, TicketType.Black).players(0).station.number should be (58)
      }
      "validate mrx black (underground) move" in {
        controller.move(46, TicketType.Black).players(0).station.number should be (46)
      }
      "not validate mrx taxi move to a not empty station" in {
        controller.move(8, TicketType.Taxi).players(0).station.number should be (1)
      }
      "not validate mrx taxi move to station not in bounds" in {
        controller.move(69, TicketType.Taxi).players(0).station.number should be (1)
      }
      "not validate mrx taxi move to the same station" in {
        controller.move(1, TicketType.Taxi).players(0).station.number should be (1)
        controller.move(1, TicketType.Taxi).players(0).tickets.taxiTickets should be (99)
      }
      "win detectives" in {
        controller.move(9, TicketType.Taxi).players(0).station.number should be (9)
        controller.move(18, TicketType.Taxi)
        val gameModel = controller.move(9, TicketType.Taxi)
        gameModel.round should be(4)
        gameModel.win should be (true)
      }
    }

    "winGame" should {
      "return true" in {
        controller.winGame(gameModel.players(0)) should be (true)
      }
    }

    "undoMove" should {
      "undo" in {
        controller.move(9, TicketType.Taxi)
        controller.undoMove().players(0).station.number should be(1)
      }
    }

    "redoMove" should {
      "undo" in {
        controller.move(9, TicketType.Taxi)
        controller.undoMove()
        controller.redoMove().players(0).station.number should be (9)
      }
    }

    "startGame" should {
      "start the game" in {
        controller.startGame() should be(true)
      }
    }

    "get" should {
      "return currentPlayer from getCurrentPlayer" in {
        controller.getCurrentPlayer.name shouldBe "MrX"
      }
      "getMrX" in {
        controller.getMrX should be(gameModel.getMrX(gameModel.players))
      }
      "return 3 from getPlayerList method" in {
        controller.getPlayersList().length shouldBe 3
      }
      "return stations from getStations method" in {
        // 199 + zero index station
        controller.getStations().length shouldBe 200
      }
      "and getTotalRound" in {
        controller.getTotalRound() should be(gameModel.totalRound)
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
      "return true from setPlayerName" in {
        controller.setPlayerName("Tim", 1) shouldBe true
      }
      "and setPlayerColor" in {
        controller.setPlayerColor("#ffffff", 1) should be (Color.decode("#ffffff"))
      }
      "and update Lobby" in {
        controller.updateLobby() should be (true)
      }
    }
  }
}
