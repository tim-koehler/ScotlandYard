package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.map.StationType.StationType
import de.htwg.se.scotlandyard.model.player.Player

class Station(var number: Integer, var sType: StationType,
                   var neighbourTaxis: Set[Station], var neighbourBuses: Set[Station], var neighbourUndergrounds: Set[Station]) {

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