package de.htwg.se.scotlandyard.model.coreComponent.station

import de.htwg.se.scotlandyard.model.{Station, StationType}
import de.htwg.se.scotlandyard.model.coreComponent.station._
import org.scalatest.{Matchers, WordSpec}

class StationSpec extends WordSpec with Matchers {
  "Station" should {
    "new Station" in {
      val station = new Station(0, StationType.Taxi)
      station.setNeighbourUndergrounds(Set()) should be (0)
      the [Exception] thrownBy station.setNeighbourBuses(Set(new Station(1, StationType.Taxi)) ) should have message "Taxi stations can't have Bus neighbours"
      the [Exception] thrownBy station.setNeighbourUndergrounds(Set(new Station(1, StationType.Taxi))) should have message "Bus or Taxi stations can't have Underground neighbours"
      station.setNeighbourTaxis(Set(station)) should be(1)

      station.getNeighbourTaxis should be(Set(station))
      station.getNeighbourBuses should be(Set())
      station.getNeighbourUndergrounds should be(Set())

      val station2 = new Station(0, StationType.Bus)
      station2.getNeighbourUndergrounds should be(Set())
    }
  }
}

