package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.ScotlandYard
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
        GameInitializer.initialize()
        GameInitializer.initPlayers(2)
        tui.tuiMode = tui.TUIMODE_RUNNING
        tui.evaluateRunning("1 b") shouldBe(tui.TUIMODE_RUNNING)
      }

      "should return -1 in MainMenuMode when 2 is selected" in {
        tui.tuiMode = tui.TUIMODE_MENU
        tui.menuCounter = 0
        tui.evaluateInput(2.toString) should be (-1)
      }

      "dispMrXstartingPosition should always return false" in {
        GameInitializer.initialize()
        GameInitializer.initPlayers(2)
        tui.dispMrXstartingPosition("") shouldBe(false)
      }

      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        tui.tuiMode = tui.TUIMODE_RUNNING
        GameMaster.startGame()
        tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
      }
      "should return state QUIT when 'exit' is inserted" in {
        tui.tuiMode = tui.TUIMODE_RUNNING
        tui.evaluateMoveMapInput("exit") should be (tui.TUIMODE_QUIT)
      }

      "evaluateSettings should return false" in {
        tui.evaluateSettings(2) shouldBe(false)
      }
    }
  }
}
