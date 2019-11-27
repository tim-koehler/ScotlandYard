package de.htwg.se.scotlandyard.model.map.station

import de.htwg.se.scotlandyard.model.map.StationType.StationType

trait Station {
  var number: Integer
  var sType: StationType
  var neighbourTaxis: Set[Station]
  var neighbourBuses: Set[Station]
  var neighbourUndergrounds: Set[Station]
  var tuiCoords: (Integer, Integer)

  def setStationType(): StationType

  def setNumber(number: Integer): Integer = {
    this.number = number
    this.number
  }

  def setCoords(coords: (Integer, Integer)): (Integer, Integer) = {
    this.tuiCoords = coords
    this.tuiCoords
  }

  def setNeighbourTaxis(neighbourTaxis: Set[Station]): Int = {
    this.neighbourTaxis = neighbourTaxis
    neighbourTaxis.size
  }

  def setNeighbourBuses(neighbourBuses: Set[Station]): Int = {
    this.neighbourBuses = neighbourBuses
    neighbourBuses.size
  }

  def setNeighbourUndergrounds(neighbourUndergrounds: Set[Station]): Int = {
    this.neighbourUndergrounds = neighbourUndergrounds
    neighbourUndergrounds.size
  }
}
