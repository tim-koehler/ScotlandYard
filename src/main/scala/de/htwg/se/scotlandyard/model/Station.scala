package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import scala.swing.Point

case class Station(val number: Integer, val stationType: StationType) {
  var neighbourTaxis: Set[Station] = Set()
  var neighbourBuses: Set[Station] = Set()
  var neighbourUndergrounds: Set[Station] = Set()
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

  override def equals(obj: Any): Boolean = {
    val comparedStation = obj.asInstanceOf[Station]
    if(number != comparedStation.number) return false
    if(tuiCoords != comparedStation.tuiCoords) return false
    if(guiCoords != comparedStation.guiCoords) return false
    true
  }
}
