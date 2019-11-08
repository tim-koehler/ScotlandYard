package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import de.htwg.se.scotlandyard.model.player.Detective
import org.scalatest._

class DetectiveSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      var st = new Station(12, StationType.Taxi, null, null, null)
      var detective = new Detective(st, "Dt1")
      "have a station number" in {
        detective.getPosition().number should be (12)
      }
      "have a station Type" in {
        detective.getPosition().sType should be (StationType.Taxi)
      }
      "return String from toString method" in {
        detective.toString() shouldBe a [String]
      }
    }
  }
}
