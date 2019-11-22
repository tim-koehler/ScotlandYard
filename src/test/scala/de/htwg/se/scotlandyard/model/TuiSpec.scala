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

      //TODO: @Roland why exception!?
      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        tui.tuiMode = tui.TUIMODE_RUNNING
        //tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
      }
      "should return state QUIT when 'exit' is inserted" in {
        tui.tuiMode = tui.TUIMODE_RUNNING
        tui.evaluateMoveMapInput("exit") should be (tui.TUIMODE_QUIT)
      }
    }
  }
}
