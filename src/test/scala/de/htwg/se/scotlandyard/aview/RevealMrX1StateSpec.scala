package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.aview.tui.{RevealMrX1State, Tui}
import de.htwg.se.scotlandyard.controllerComponent.controllerMockImpl.Controller
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOMockImpl.FileIO
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapMockImpl.TuiMap
import org.scalatest._

class RevealMrX1StateSpec extends WordSpec with Matchers {
  "RevealMrX1StateSpec" when {
    "new" should {
      val controller = new Controller(new GameInitializer, new FileIO(new GameInitializer))

      val tui = new Tui(controller, new TuiMap)
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
