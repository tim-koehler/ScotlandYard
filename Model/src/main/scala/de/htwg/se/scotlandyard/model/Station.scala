package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.StationType.StationType

import scala.swing.Point

case class Station(number: Integer = -1,
                   stationType: StationType = StationType.Taxi,
                   blackStation: Boolean = false,
                   neighbourTaxis: Set[Station] = Set(),
                   neighbourBuses: Set[Station] = Set(),
                   neighbourUndergrounds: Set[Station] = Set(),
                   tuiCoordinates: Point = new Point(1, 1),
                   guiCoordinates: Point = new Point(1, 1))
{

  def setNeighbourTaxis(station: Station, neighbours: Set[Station]): Option[Station] = {
    Some(station.copy(neighbourTaxis = neighbours))
  }

  def setNeighbourBuses(station: Station, neighbours: Set[Station]): Option[Station] = {
    if(neighbours.isEmpty) {
      return Some(station)
    }
    if(station.stationType < StationType.Bus) {
      return None
    }
    Some(station.copy(neighbourBuses = neighbours))
  }

  def setNeighbourUndergrounds(station: Station, neighbours: Set[Station]): Option[Station] = {
    if(neighbours.isEmpty) {
      return Some(station)
    }
    if(station.stationType < StationType.Underground) {
      return None
    }
    Some(station.copy(neighbourUndergrounds = neighbours))
  }
}