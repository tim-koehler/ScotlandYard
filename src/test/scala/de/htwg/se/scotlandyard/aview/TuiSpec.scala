package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "created" should {
      GameMaster.startGame()
      val tui = new Tui(new Controller())
      "refresh the screen when input is invalid in mainMenu and not refresh" in {
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
      }

      "evaluateInput should return -1 when exit game is selected" in {
        tui.evaluateInput("2") shouldBe(tui.TUIMODE_QUIT)
      }

      "change the number of player or refresh the screen" in {
        GameMaster.startGame()
        tui.changeState(new SelectNumberOfPlayerMenuState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateInput("2") shouldBe (2)
      }

      "should display all players and their names" in {
        tui.changeState(new ChooseNameMenuState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateInput("2") shouldBe (2)
      }

      "should take input for a player name and go back to chooseNameMenu" in {
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("Uff") shouldBe (1)
      }

      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        GameMaster.startGame()
        tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
      }
      "should return state QUIT when 'exit' is inserted" in {
        tui.evaluateMoveMapInput("exit") should be (tui.TUIMODE_QUIT)
      }

      "should return true when revealMrx1 is called" in {
        tui.revealMrX1() shouldBe(0)
      }

      "should return true when revealMrx2 is called" in {
        tui.revealMrX2() shouldBe(0)
      }

      "should return true when evaluateEnterName is called" in {
        tui.indexOfPlayerWhichNameToChange = 1
        tui.evaluateEnterName("Uff") shouldBe (true)
      }

    }
  }
}
