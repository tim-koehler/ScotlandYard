package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.aview.Tui
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.GameMaster
import org.scalatest.{Matchers, WordSpec}

class ControllerSpec extends WordSpec with Matchers{
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
    }
  }
}
