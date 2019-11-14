package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.{Station, StationType}

class MrX(var station: Station, var name: String = "MrX") extends Player {
  var blackTickets: Int = 5 // default: 5
  var doubleTurns: Int = 2 // default: 2
  var hidden: Boolean = true
  var lastSeen: String = "never"

  override def toString(): String = {
    if(hidden) {
      name + " (hidden) was" + " Last seen: " + lastSeen
    } else {
      name + " is at " + station.number
    }
  }
}
