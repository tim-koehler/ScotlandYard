package de.htwg.se.scotlandyard.model.player

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.map.station.{Station, StationFactory}
import org.scalatest._

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      StationFactory.createZeroIndexStation()
      val st = StationFactory.createTaxiStation(0, (1,1))
      val mrX = new MrX(st, "MrX")
      "have a station number" in {
        mrX.getPosition().number should be (0)
      }
      "have a station Type" in {
        mrX.getPosition().sType should be (StationType.Taxi)
      }
      "should have a nice String representation when visible" in {
        mrX.isVisible = false
        mrX.toString() shouldNot be (null)
      }
      "should have a nice String representation when hidden" in {
        mrX.isVisible = true
        mrX.toString() shouldNot be (null)
      }
    }
  }
}
