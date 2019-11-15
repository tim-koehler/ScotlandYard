package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.aview.Tui
import de.htwg.se.scotlandyard.controller.Controller
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "new" should {
      val tui = new Tui(new Controller())
      "return 3 in settingsmode when a number >= 2 && <= 7 is selected" in {
        tui.evaluateSettings(2.toString) should be (3)
      }

      "should return 2 in MainMenuMode when 1 is selected" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(1.toString) should be (2)
      }

      "should return -1 in MainMenuMode when 2 is selected" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(2.toString) should be (-1)
      }
    }
  }
}
