package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.MrXInterface
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class MrX(var mrXstation: Station) extends Detective(mrXstation, "MrX", Color.BLACK) with MrXInterface  {
  override var blackTickets: Int = 5
  override var doubleTurn: Int = 2
  override var isVisible: Boolean = false
  override var lastSeen: String = "never"
  override var history: List[TicketType] = List()
  override val lastSeenColor: Color = Color.GRAY

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
