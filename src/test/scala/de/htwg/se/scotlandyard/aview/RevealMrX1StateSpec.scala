package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.aview.tui.{RevealMrX1State, Tui}
import de.htwg.se.scotlandyard.controller.Controller
import de.htwg.se.scotlandyard.model.core.{GameInitializer, GameMaster}
import org.scalatest._

class RevealMrX1StateSpec extends WordSpec with Matchers {
  "RevealMrX1StateSpec" when {
    "new" should {
      GameMaster.startGame()
      GameInitializer.initPlayers(2)
      val tui = new Tui(new Controller())
      tui.changeState(new RevealMrX1State(tui))

      "RevealMrX1State evalInput should return 0" in {
        tui.state.evaluateInput(2.toString) shouldBe(tui.TUIMODE_RUNNING)
      }

      "RevealMrX1State toString should return a String" in {
        tui.state.toString shouldBe a[String]
      }
    }
  }
}
