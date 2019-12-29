package de.htwg.se.scotlandyard.model.player

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class MrX(var MrXstation: Station) extends Player(MrXstation, "MrX", Color.BLACK)  {
  var blackTickets: Int = 5 // default: 5
  var doubleTurn: Int = 2 // default: 2
  var isVisible: Boolean = false
  var lastSeen: String = "never"
  var history: List[TicketType] = List()
  val lastSeenColor: Color = Color.GRAY

  override def toString(): String = {
    val tickets = " - BLACKTICKETS: " + blackTickets + ", DOUBLETURNS: " + doubleTurn
    if(isVisible) {
      name + " is at " + station.number + tickets
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen + tickets
    }
  }

  def getHistory(): List[TicketType] = {
    this.history
  }

  def addToHistory(ticket: TicketType): Boolean = {
    history = history ::: List(ticket)
    true
  }

  def removeFromHistory(): Boolean = {
    if(history.isEmpty) {
      false
    } else {
      history = history.drop(1)
      true
    }
  }
}
