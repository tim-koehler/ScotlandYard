package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import de.htwg.se.scotlandyard.model.player.TicketType
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      GameMaster.startGame()
      GameInitializer.initPlayers(2)
      val controller = new Controller()
      "should return 2 from getPlayerList method" in {
        controller.getPlayersList().length shouldBe (2)
      }
      "should return true from setPlayerNames when index is correct" in {
        controller.setPlayerNames("Tim", 1) shouldBe true
      }
      "should 2 from nextRound Method" in {
        GameMaster.round = 1
        controller.nextRound() shouldBe(2)
      }
      "should return the correct player from setPlayerNumber" in {
        controller.initPlayers(2) shouldBe (2)
      }
      "should validateAndMove" in {
        GameMaster.getCurrentPlayer().station =  GameMaster.stations(1)
        GameMaster.players.head.station = GameMaster.stations(3)

        controller.doValidateAndMove(2, TicketType.Taxi) should be(GameMaster.stations(2))
        controller.undoValidateAndMove() should be(GameMaster.stations(1))
        controller.redoValidateAndMove() should be(GameMaster.stations(2))

        for (p <- GameMaster.players){
          p.station = GameMaster.stations(1)
        }
        GameMaster.getCurrentPlayer().station = GameMaster.stations(2)

        controller.doValidateAndMove(3, TicketType.Bus) should be(GameMaster.stations(3))
        controller.undoValidateAndMove() should be(GameMaster.stations(2))

        controller.doValidateAndMove(3, TicketType.Underground) should be(GameMaster.stations(3))
        controller.undoValidateAndMove() should be(GameMaster.stations(2))
      }
      "and updateMrXVisibility" in {
        controller.updateMrXVisibility() should be (false)
      }
    }
  }
}
