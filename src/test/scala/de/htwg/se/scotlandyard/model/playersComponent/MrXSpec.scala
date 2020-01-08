package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.util.StationType
import org.scalatest._

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      StationFactory.createZeroIndexStation()
      val st = StationFactory.createTaxiStation(0, (1,1))
      val mrX = new MrX()
      mrX.station = st

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
