package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import de.htwg.se.scotlandyard.model.player.Detective
import org.scalatest._

class DetectiveSpec extends WordSpec with Matchers {
  "Detective" when {
    "new" should {
      val st = new Station(2, StationType.Taxi, Set(), Set(), Set())
      val dt1 = new Detective(st, "Dt1")

      "have a station number" in {
        dt1.getPosition().number should be (2)
      }
      "have a station Type" in {
        dt1.getPosition().sType should be (StationType.Taxi)
      }
      "should have a nice String representation" in {
        dt1.toString() shouldBe ("Dt1: 2 (TAXI)  TICKETS-> T: 0, B: 0, U: 0")
      }
    }
  }
}
