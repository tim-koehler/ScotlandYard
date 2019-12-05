package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.StationType
import de.htwg.se.scotlandyard.model.map.station.Station

class MrX(var station: Station, var name: String = "MrX") extends Player {
  var blackTickets: Int = 5 // default: 5
  var doubleTurns: Int = 2 // default: 2
  var isVisible: Boolean = ScotlandYard.isDebugMode
  var lastSeen: String = "never"

  override def toString(): String = {
    var tickets = " - BLACKTICKETS: " + blackTickets + ", DOUBLETURNS: " + doubleTurns
    if(isVisible) {
      name + " is at " + station.number + tickets
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen + tickets
    }
  }
}
