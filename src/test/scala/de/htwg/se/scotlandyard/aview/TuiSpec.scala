package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl.TuiMap
import de.htwg.se.scotlandyard.aview.tui.{ChooseNameMenuState, EnterNameState, RevealMrX1State, RevealMrX2State, RunningState, SelectNumberOfPlayerMenuState, Tui, WinningState}
import de.htwg.se.scotlandyard.controller.controllerMockImpl.Controller
import de.htwg.se.scotlandyard.controller.fileIoComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest._

class TuiSpec extends WordSpec with Matchers {
  "Tui" when {
    "created" should {
      val gameInitializer = new GameInitializer

      val controller = new Controller(gameInitializer, new FileIO(gameInitializer))

      val tui = new Tui(controller, new TuiMap)

      "refresh the screen when input is invalid in mainMenu and not refresh" in {
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateInput("load") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("exit") shouldBe(tui.TUIMODE_QUIT)
      }
      "change the number of player or refresh the screen" in {
        controller.initialize()
        tui.changeState(new SelectNumberOfPlayerMenuState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateInput("3") shouldBe (3)
      }
      "should display all players and their names" in {
        tui.changeState(new ChooseNameMenuState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateInput("1") shouldBe (1)
        tui.changeState(new ChooseNameMenuState(tui))
        tui.evaluateInput("2") shouldBe (2)
      }
      "should take input for a player name and go back to chooseNameMenu" in {
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("Uffi") shouldBe (1)
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("h") shouldBe (tui.TUIMODE_RUNNING)
      }
      "should move the map and refresh the screen or move the player and refresh the screen" in {
        tui.changeState(new RunningState(tui))
        tui.evaluateInput("1 T") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("1 B") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("1 U") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("1 x") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("WW") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("undo") should be(tui.TUIMODE_RUNNING)
        tui.evaluateInput("redo") should be(tui.TUIMODE_RUNNING)
        tui.evaluateInput("save") should be(tui.TUIMODE_RUNNING)
        tui.evaluateUndo() shouldBe(tui.TUIMODE_RUNNING)
      }
      "should just update screen to show that MrX will be revealed" in {
        tui.changeState(new RevealMrX1State(tui))
        tui.state.toString should not be ("")
        tui.evaluateInput("") shouldBe(tui.TUIMODE_RUNNING)
      }
      "should just update screen to display the position of MrX" in {
        tui.changeState(new RevealMrX2State(tui))
        tui.evaluateInput("") shouldBe(tui.TUIMODE_RUNNING)
        tui.state shouldBe a[RunningState]
      }
      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        controller.initialize()
        tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("w") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("s") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("d") should be (tui.TUIMODE_RUNNING)
      }
      "should return RUNNING when 'undo' is inserted" in {
        the [Exception] thrownBy  tui.evaluateNextPositionInput("undo")  should not have message("")
      }
      "should update the screen when evaluateNextPositionInput is called" in {
        tui.evaluateNextPositionInput("999 t") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateNextPositionInput("999 b") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateNextPositionInput("999 u") shouldBe (tui.TUIMODE_RUNNING)
        tui.evaluateNextPositionInput("999 x") shouldBe (tui.TUIMODE_RUNNING)
      }
      "should return 0 when a revealMrx method is called" in {
        tui.revealMrX1() shouldBe(tui.TUIMODE_RUNNING)
        tui.revealMrX2() shouldBe(tui.TUIMODE_RUNNING)
      }
      "should evaluate correct in winningState" in {
        tui.changeState(new WinningState(tui))
        tui.state.evaluateInput("undo") should be(tui.TUIMODE_RUNNING)
        tui.state.evaluateInput("redo") should be(tui.TUIMODE_RUNNING)
        tui.state.evaluateInput("ufgd") should be(tui.TUIMODE_QUIT)
      }
      "should return TUIMODE_RUNNING when evaluateRedo is called" in {
        tui.evaluateRedo() should be(tui.TUIMODE_RUNNING)
      }
      "should return TUIMODE_RUNNING when evaluateSave is called" in {
        tui.evaluateSave() should be(tui.TUIMODE_RUNNING)
      }
      "should return TUIMODE_RUNNING when evaluateLoad is called" in {
        tui.evaluateLoad() should be(tui.TUIMODE_RUNNING)
      }
    }
  }
}
