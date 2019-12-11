package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.aview.tui.{ChooseNameMenuState, EnterNameState, MainMenuState, RevealMrX1State, RevealMrX2State, RunningState, SelectNumberOfPlayerMenuState, Tui}
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
        tui.evaluateInput("1") shouldBe (1)
        tui.changeState(new ChooseNameMenuState(tui))
        tui.evaluateInput("2") shouldBe (2)
      }
      "should take input for a player name and go back to chooseNameMenu" in {
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("Uff") shouldBe (1)
        tui.changeState(new EnterNameState(tui))
        tui.evaluateInput("") shouldBe (tui.TUIMODE_RUNNING)
      }
      "should move the map and refresh the screen or move the player and refresh the screen" in {
        tui.changeState(new RunningState(tui))
        tui.evaluateInput("1 T") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("1 B") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("1 U") shouldBe(tui.TUIMODE_RUNNING)
        tui.evaluateInput("WW") shouldBe(tui.TUIMODE_RUNNING)
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
      "evaluateMainMenuMethod should return number of players, running or -1" in {
        tui.changeState(new MainMenuState(tui))
        tui.evaluateMainMenu(1) shouldBe(0)
        tui.changeState(new MainMenuState(tui))
        tui.evaluateMainMenu(2) shouldBe(tui.TUIMODE_QUIT)
        tui.changeState(new MainMenuState(tui))
        tui.evaluateMainMenu(1) shouldBe(tui.TUIMODE_RUNNING)
      }
      "should return state RUNNING when 'a', 'w', 's' or 'd' is pressed is pressed" in {
        GameMaster.startGame()
        tui.evaluateMoveMapInput("a") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("w") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("s") should be (tui.TUIMODE_RUNNING)
        tui.evaluateMoveMapInput("d") should be (tui.TUIMODE_RUNNING)
      }
      "should return state QUIT when 'exit' is inserted" in {
        tui.evaluateMoveMapInput("exit") should be (tui.TUIMODE_QUIT)
      }
      "should return RUNNING when 'undo' is inserted" in {
        the [Exception] thrownBy  tui.evaluateNextPositionInput("undo")  should not have message("")
      }
      "should return 0 when a revealMrx method is called" in {
        tui.revealMrX1() shouldBe(tui.TUIMODE_RUNNING)
        tui.revealMrX2() shouldBe(tui.TUIMODE_RUNNING)
      }
      "should have a winning output String when a player wins" in {
        //GameInitializer.initStations()
        GameInitializer.initPlayers(2)
        GameMaster.winningPlayer = GameMaster.players(0)
        tui.buildOutputStringWin() shouldNot be (null)
        GameMaster.winningPlayer = GameMaster.players(1)
        tui.buildOutputStringWin() shouldNot be (null)
      }
      "should return 0 when evaluateWinningMethod is called" in {
        tui.evaluateWinning("") shouldBe (tui.TUIMODE_RUNNING)
      }
    }
  }
}
