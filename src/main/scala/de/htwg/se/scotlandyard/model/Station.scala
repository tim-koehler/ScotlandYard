package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import java.awt.Point

class Station(val number: Integer, val stationType: StationType) {
  private var neighbourTaxis: Set[Station] = Set()
  private var neighbourBuses: Set[Station] = Set()
  private var neighbourUndergrounds: Set[Station] = Set()
  var tuiCoords: Point = new Point(1, 1)
  var guiCoords: Point = new Point(1, 1)

  def setNeighbourTaxis(neighbours: Set[Station]): Integer = {
    if(neighbours.isEmpty) {
      return neighbours.size
    }
    this.neighbourTaxis = neighbours
    neighbours.size
  }

  def setNeighbourBuses(neighbours: Set[Station]): Integer = {
    if(neighbours.isEmpty) {
      return neighbours.size
    }
    if(stationType < StationType.Bus) {
      throw new Exception("Taxi stations can't have Bus neighbours")
    }
    this.neighbourBuses = neighbours
    neighbours.size
  }

  def setNeighbourUndergrounds(neighbours: Set[Station]): Integer = {
    if(neighbours.isEmpty) {
      return neighbours.size
    }
    if(stationType < StationType.Underground) {
      throw new Exception("Bus or Taxi stations can't have Underground neighbours")
    }
    this.neighbourUndergrounds = neighbours
    neighbours.size
  }

  def getNeighbourTaxis: Set[Station] = neighbourTaxis
  def getNeighbourBuses: Set[Station] = neighbourBuses
  def getNeighbourUndergrounds: Set[Station] = neighbourUndergrounds
}
