package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.{Station, StationType}

class MrX(station: Station) extends Player {
  val name = "MrX"
  override def getPosition(): Station = {
    station
  }

  override def toString(): String = {
    name + ": " + station.number + " " + station.sType.toString.toUpperCase + " TICKETS-> T: " + taxiTickets + ", B: " + busTickets + ", U: " + undergroundTickets
  }

}
