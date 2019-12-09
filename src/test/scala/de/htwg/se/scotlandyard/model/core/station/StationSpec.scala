package de.htwg.se.scotlandyard.model.core.station

import de.htwg.se.scotlandyard.model.core.station._
import de.htwg.se.scotlandyard.model.map.station.StationFactory
import org.scalatest.{Matchers, WordSpec}

class StationSpec extends WordSpec with Matchers {
  "Station" should {
    "new Station" in {
      val station = StationFactory.createTaxiStation(0,(1,1))
      the [Exception] thrownBy station.setNeighbourBuses(Set()) should have message "Station doesn't have Bus neighbours!"
      the [Exception] thrownBy station.setNeighbourUndergrounds(Set()) should have message "Station doesn't have Underground neighbours!"
      station.setNeighbourTaxis(Set(station)) should be(1)

      station.neighbourTaxis should be(Set(station))
      station.neighbourBuses should be(null)
      station.neighbourUndergrounds should be(null)

      val station2 = StationFactory.createBusStation(0, (1,1))
      station2.neighbourUndergrounds should be(null)
    }
  }
}

