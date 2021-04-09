package de.htwg.se.scotlandyard.model

import org.scalatest.{Matchers, WordSpec}
import de.htwg.se.scotlandyard.model.Station
import de.htwg.se.scotlandyard.model.StationType


class StationSpec extends WordSpec with Matchers {
  "Station" should {
    "new Station" in {
      val station = Station(1, StationType.Taxi)
      station.setNeighbourTaxis(station, Set()).get.neighbourTaxis.isEmpty should be (true)
      station.setNeighbourBuses(Station(1, StationType.Taxi), Set(Station(2, StationType.Bus))) should not be("defined")
      station.setNeighbourUndergrounds(Station(1, StationType.Bus), Set(Station(2, StationType.Underground))) should not be("defined")
      station.setNeighbourTaxis(station, Set(Station(2,StationType.Taxi))).get.neighbourTaxis.size should be(1)
    }
  }
}

