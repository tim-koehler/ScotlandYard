package de.htwg.se.scotlandyard.model.tuiMapComponent.station
import java.awt.Point

import de.htwg.se.scotlandyard.util.StationType
import de.htwg.se.scotlandyard.util.StationType.StationType

class BusStation(num: Integer, tCoords: (Integer, Integer)) extends Station {
  override val number: Integer = num
  override var sType: StationType = StationType.Bus
  override var neighbourTaxis: Set[Station] = _
  override var neighbourBuses: Set[Station] = _
  override var neighbourUndergrounds: Set[Station] = _
  override var tuiCoords: (Integer, Integer) = tCoords
  override var guiCoords: Point = _

  override def setNeighbourBuses(neighbourBuses: Set[Station]): Integer = {
    this.neighbourBuses = neighbourBuses
    neighbourBuses.size
  }
}