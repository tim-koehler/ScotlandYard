package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster
import org.scalatest._

class ControllerSpec extends WordSpec with Matchers {
  "Controller" when {
    "new" should {
      GameMaster.startGame()
      val controller = new Controller()
      "should return 7 or 2 from getPlayerList method" in {
        if(ScotlandYard.isDebugMode) controller.getPlayersList().length shouldBe (2)
        else controller.getPlayersList().length shouldBe (7)
      }
      "should return true from setPlayerNames when index is correct" in {
        controller.setPlayerNames("Tim", 1) shouldBe true
      }
      "should 2 from nextRound Method" in {
        GameMaster.round = 1
        controller.nextRound() shouldBe(2)
      }
      "should return the correct player from setPlayerNumber" in {
        controller.setPlayerNumber(2) shouldBe (2)
      }
    }
  }
}
