package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import java.awt.Point

class Station(val number: Integer, val stationType: StationType) {
  private var neighbourTaxis: Set[Station] = Set()
  private var neighbourBuses: Set[Station] = Set()
  private var neighbourUndergrounds: Set[Station] = Set()
  var tuiCoords: Point = new Point(0, 0)
  var guiCoords: Point = new Point(0, 0)

  def setNeighbourTaxis(neighbourTaxis: Set[Station]): Integer = {
    this.neighbourTaxis = neighbourTaxis
    neighbourTaxis.size
  }

  def setNeighbourBuses(neighbourBuses: Set[Station]): Integer = {
    if(stationType < StationType.Bus) {
      throw new Exception("Taxi stations can't have Bus neighbours")
    }
    this.neighbourBuses = neighbourBuses
    neighbourBuses.size
  }

  def setNeighbourUndergrounds(neighbourUndergrounds: Set[Station]): Integer = {
    if(stationType < StationType.Underground) {
      throw new Exception("Bus or Taxi stations can't have Underground neighbours")
    }
    this.neighbourUndergrounds = neighbourUndergrounds
    neighbourUndergrounds.size
  }

  def getNeighbourTaxis: Set[Station] = neighbourTaxis
  def getNeighbourBuses: Set[Station] = neighbourBuses
  def getNeighbourUndergrounds: Set[Station] = neighbourUndergrounds
}
