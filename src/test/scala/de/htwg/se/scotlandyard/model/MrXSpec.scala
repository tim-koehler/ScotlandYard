package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.model.map._
import org.scalatest._

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      var st = new Station(12, StationType.Taxi, null, null, null)
      var MrX = new MrX(st, "MrX")
      "have a station number" in {
        MrX.getPosition().number should be (12)
      }
      "have a station Type" in {
        MrX.getPosition().sType should be (StationType.Taxi)
      }
      "return String from toString method" in {
        MrX.toString() shouldBe a [String]
      }
    }
  }
}
