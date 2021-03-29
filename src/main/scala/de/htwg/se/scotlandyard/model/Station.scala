package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import scala.swing.Point

case class Station(number: Integer = -1,
                   stationType: StationType = StationType.Taxi,
                   neighbourTaxis: Set[Station] = Set(),
                   neighbourBuses: Set[Station] = Set(),
                   neighbourUndergrounds: Set[Station] = Set(),
                   tuiCoordinates: Point = new Point(1, 1),
                   guiCoordinates: Point = new Point(1, 1))
{

  def setNeighbourTaxis(station: Station, neighbours: Set[Station]): Station = {
    station.copy(neighbourTaxis = neighbours)
  }

  def setNeighbourBuses(station: Station, neighbours: Set[Station]): Station = {
    if(neighbours.isEmpty) {
      return station
    }
    if(station.stationType < StationType.Bus) {
      throw new Exception("Taxi stations can't have Bus neighbours")
    }
    station.copy(neighbourBuses = neighbours)
  }

  def setNeighbourUndergrounds(station: Station, neighbours: Set[Station]): Station = {
    if(neighbours.isEmpty) {
      return station
    }
    if(station.stationType < StationType.Underground) {
      throw new Exception("Bus or Taxi stations can't have Underground neighbours")
    }
    station.copy(neighbourUndergrounds = neighbours)
  }

  override def equals(obj: Any): Boolean = {
    val comparedStation = obj.asInstanceOf[Station]
    if(number != comparedStation.number) return false
    if(tuiCoordinates != comparedStation.tuiCoordinates) return false
    if(guiCoordinates != comparedStation.guiCoordinates) return false
    true
  }
}
