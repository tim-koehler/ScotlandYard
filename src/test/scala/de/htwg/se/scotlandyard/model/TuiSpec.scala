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

      "return 1" in {
        tui.evaluateInput(0.toString) should be (99)
        tui.evaluateInput(1.toString) should be (99)
        tui.evaluateInput(2.toString) should be (99)
      }

    }
  }
}
