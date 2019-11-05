package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.Station

class Detective(station: Station, name : String) extends Player {
  override def getPosition(): Station = {
    station
  }

  override def toString(): String = {
    name + ": " + station.number + " " + station.sType.toString().toUpperCase + " TICKETS-> T: " + taxiTickets + " B: " + busTickets + " U: " + undergroundTickets
  }

}
