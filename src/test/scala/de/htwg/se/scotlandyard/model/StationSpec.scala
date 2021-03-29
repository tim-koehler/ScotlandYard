package de.htwg.se.scotlandyard.model

import org.scalatest.{Matchers, WordSpec}

class StationSpec extends WordSpec with Matchers {
  "Station" should {
    "new Station" in {
      val station = Station(1, StationType.Taxi)
      station.setNeighbourTaxis(station, Set()) .neighbourTaxis.isEmpty should be (true)
      the [Exception] thrownBy station.setNeighbourBuses(Station(1, StationType.Taxi), Set(Station(2, StationType.Bus))) should have message "Taxi stations can't have Bus neighbours"
      the [Exception] thrownBy station.setNeighbourUndergrounds(Station(1, StationType.Bus), Set(Station(2, StationType.Underground))) should have message "Bus or Taxi stations can't have Underground neighbours"
      station.setNeighbourTaxis(station, Set(Station(2,StationType.Taxi))).neighbourTaxis.size should be(1)
    }
  }
}

