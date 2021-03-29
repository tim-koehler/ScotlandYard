package de.htwg.se.scotlandyard.model

import org.scalatest.{Matchers, WordSpec}

class StationSpec extends WordSpec with Matchers {
  "Station" should {
    "new Station" in {
      val station = Station(0, StationType.Taxi)
      station.setNeighbourUndergrounds(Set()) should be (0)
      the [Exception] thrownBy station.setNeighbourBuses(Set(new Station(1, StationType.Taxi)) ) should have message "Taxi stations can't have Bus neighbours"
      the [Exception] thrownBy station.setNeighbourUndergrounds(Set(new Station(1, StationType.Taxi))) should have message "Bus or Taxi stations can't have Underground neighbours"
      station.setNeighbourTaxis(Set(station)) should be(1)

      station.neighbourTaxis should be(Set(station))
      station.neighbourBuses should be(Set())
      station.neighbourUndergrounds should be(Set())

      val station2 = new Station(0, StationType.Bus)
      station2.neighbourUndergrounds should be(Set())
    }
  }
}

