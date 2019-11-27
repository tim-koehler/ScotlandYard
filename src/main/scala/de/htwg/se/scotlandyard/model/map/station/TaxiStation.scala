package de.htwg.se.scotlandyard.model.map.station
import de.htwg.se.scotlandyard.model.map.StationType
import de.htwg.se.scotlandyard.model.map.StationType.StationType

class TaxiStation(num: Integer, tCoords: (Integer, Integer)) extends Station {
  override var number: Integer = num
  override var sType: StationType = _
  override var neighbourTaxis: Set[Station] = _
  override var neighbourBuses: Set[Station] = _
  override var neighbourUndergrounds: Set[Station] = _
  override var tuiCoords: (Integer, Integer) = tCoords

  override def setStationType(): StationType = {
    this.sType = StationType.Taxi
    this.sType
  }
}
