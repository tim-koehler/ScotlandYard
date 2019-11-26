package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "new" should {
      val tui = new Tui(new Controller())
      "should bring you to player settings menu when input is 1" in {
        tui.evaluateInput(1.toString) shouldBe(tui.TUIMODE_MENU)
      }

      "evaluate Running should return TUIMODE_RUNNING (0)" in {
        //tui.tuiMode = tui.TUIMODE_RUNNING
        //tui.evaluateRunning("1 b") shouldBe(tui.TUIMODE_RUNNING)
      }

      "should return -1 in MainMenuMode when 2 is selected" in {
        //tui.tuiMode = tui.TUIMODE_MAINMENU
        //tui.evaluateInput(2.toString) should be (-1)
      }

      "dispMrXstartingPosition should always return 4" in {
        //GameInitializer.initPlayers(2)
        //tui.dispMrXstartingPosition("") shouldBe(4)
      }

      //TODO: @Roland why exception!?
      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        //tui.tuiMode = tui.TUIMODE_RUNNING
        //GameMaster.startGame()
        //tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
      }
      "should return state QUIT when 'exit' is inserted" in {
        tui.tuiMode = tui.TUIMODE_RUNNING
        tui.evaluateMoveMapInput("exit") should be (tui.TUIMODE_QUIT)
      }
    }
  }
}
