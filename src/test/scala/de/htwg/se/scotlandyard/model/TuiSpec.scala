package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.aview.Tui
import de.htwg.se.scotlandyard.controller.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "new" should {
      val tui = new Tui(new Controller())
      "return a String" in {
        //tui.getMainMenuString() shouldBe a[String]
      }

      "should return 0 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(1.toString) should be (3)
      }

      "should return 2 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(2.toString) should be (2)
      }

      "should return 3 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(3.toString) should be (-1)
      }
    }
  }
}
