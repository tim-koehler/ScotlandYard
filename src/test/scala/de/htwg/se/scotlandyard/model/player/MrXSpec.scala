package de.htwg.se.scotlandyard.model.player

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.map.station.Station
import org.scalatest._

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      val st = new Station(1, StationType.Taxi, Set(), Set(), Set(), (1, 1))
      val mrX = new MrX(st, "MrX")
      "have a station number" in {
        mrX.getPosition().number should be (1)
      }
      "have a station Type" in {
        mrX.getPosition().sType should be (StationType.Taxi)
      }
      "should have a nice String representation when visible" in {
        mrX.isVisible = false
        mrX.toString() shouldBe ("MrX (hidden) was Last seen: never")
      }
      "should have a nice String representation when hidden" in {
        mrX.isVisible = true
        mrX.toString() shouldBe ("MrX is at 1")
      }
    }
  }
}
