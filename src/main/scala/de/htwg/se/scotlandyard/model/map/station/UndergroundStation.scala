package de.htwg.se.scotlandyard.model.map.station

import de.htwg.se.scotlandyard.model.map.StationType
import de.htwg.se.scotlandyard.model.map.StationType.StationType

class UndergroundStation(num: Integer, tCoords: (Integer, Integer)) extends Station {
  override val number: Integer = num
  override var sType: StationType = StationType.Underground
  override var neighbourTaxis: Set[Station] = _
  override var neighbourBuses: Set[Station] = _
  override var neighbourUndergrounds: Set[Station] = _
  override var tuiCoords: (Integer, Integer) = tCoords

  override def setNeighbourBuses(neighbourBuses: Set[Station]): Integer = {
    this.neighbourBuses = neighbourBuses
    neighbourBuses.size
  }

  override def setNeighbourUndergrounds(neighbourUndergrounds: Set[Station]): Integer = {
    this.neighbourUndergrounds = neighbourUndergrounds
    neighbourUndergrounds.size
  }
}
