package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import org.scalatest._

class StationSpec extends WordSpec with Matchers {
  "A Station" when {
    "new" should {
      val station = Station(4, StationType.Bus)
      "have a number" in {
        station.number should be(4)
      }
      "have a Station Type" in {
        station.sType should be(StationType.Bus)
      }
    }
  }
}