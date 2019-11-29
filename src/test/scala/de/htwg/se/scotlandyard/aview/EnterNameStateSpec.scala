package de.htwg.se.scotlandyard.aview

import de.htwg.se.scotlandyard.controller.Controller
import org.scalatest.{Matchers, WordSpec}

class EnterNameStateSpec extends WordSpec with Matchers {
  "EnterNameState" when {
    var controller = new Controller()
    var tui = new Tui(controller)
    var state = new EnterNameState(tui)
    "created" should {
      "should not return an empty String" in {

      }
    }
  }
}
