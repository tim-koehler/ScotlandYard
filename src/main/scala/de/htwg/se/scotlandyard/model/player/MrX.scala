package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.{Station, StationType}

class MrX(var station: Station, var name: String = "MrX") extends Player {
  var blackTickets: Int = 5 // default: 5
  var doubleTurns: Int = 2 // default: 2
  var isVisible: Boolean = false
  var lastSeen: String = "never"

  override def toString(): String = {
    if(isVisible) {
      name + " is at " + station.number
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen
    }
  }
}
