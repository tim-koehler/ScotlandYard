package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.core.Tui
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "new" should {
      val tui = new Tui()
      "return a String" in {
        //tui.getMainMenuString() shouldBe a[String]
      }

      "should return 0 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(1.toString) should be (0)
      }

      "should return 2 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(2.toString) should be (2)
      }

      "should return -1 in MainMenuMode" in {
        tui.tuiMode = tui.TUIMODE_MAINMENU
        tui.evaluateInput(3.toString) should be (-1)
      }

    }
  }
}
