package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers {
  "Controller" when {
    "new" should {
      GameMaster.startGame()
      val controller = new Controller()
      "should return 7 from getPlayerList method" in {
        controller.getPlayersList().length shouldBe (7)
      }
      "should return true from setPlayerNames when index is correct" in {
        controller.setPlayerNames("Tim", 5) shouldBe true
      }
      "should 2 from nextRound Method" in {
        GameMaster.round = 1
        controller.nextRound() shouldBe(2)
      }
      "should return the correct player from setPlayerNumber" in {
        controller.setPlayerNumber(3) shouldBe (3)
      }
    }
  }
}
