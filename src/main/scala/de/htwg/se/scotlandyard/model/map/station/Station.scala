package de.htwg.se.scotlandyard.model.map.station

import java.awt.Point

import de.htwg.se.scotlandyard.model.map.StationType.StationType

trait Station {
  val number: Integer
  var sType: StationType
  var neighbourTaxis: Set[Station]
  var neighbourBuses: Set[Station]
  var neighbourUndergrounds: Set[Station]
  var tuiCoords: (Integer, Integer)
  var guiCoords: Point

  def setNeighbourTaxis(neighbourTaxis: Set[Station]): Integer = {
    this.neighbourTaxis = neighbourTaxis
    neighbourTaxis.size
  }

  def setNeighbourBuses(neighbourBuses: Set[Station]): Integer = {
    throw new Exception("Station doesn't have Bus neighbours!")
  }

  def setNeighbourUndergrounds(neighbourUndergrounds: Set[Station]): Integer = {
    throw new Exception("Station doesn't have Underground neighbours!")
  }
}
