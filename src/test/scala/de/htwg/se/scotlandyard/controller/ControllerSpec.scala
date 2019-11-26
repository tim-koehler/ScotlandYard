package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers {
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
    }
  }
}
