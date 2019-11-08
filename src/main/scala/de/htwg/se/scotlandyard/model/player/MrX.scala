package de.htwg.se.scotlandyard.model.player
import de.htwg.se.scotlandyard.model.map.{Station, StationType}

class MrX(var station: Station, var name: String) extends Player {
  var blackTickets: Int = 5 // default: 5
  var doubleTurns: Int = 2 // default: 2
}
