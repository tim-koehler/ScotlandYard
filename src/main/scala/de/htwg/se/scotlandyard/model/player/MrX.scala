package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.Station

class MrX(station: Station) extends Player {
  override def getPosition(): Station = {
    station
  }
}
