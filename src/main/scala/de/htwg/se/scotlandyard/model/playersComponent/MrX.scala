package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.{Station, Tickets}
import java.awt.Color

case class MrX(override val station: Station = Station(),
               override val tickets: Tickets = Tickets(99, 99, 99, 5),
               override val name: String = "MrX",
               override val color: Color = Color.BLACK,
               isVisible: Boolean = false,
               lastSeen: String = "never",
               history: List[TicketType] = List()) extends Player(station,name,color,tickets)
{
  val lastSeenColor: Color = Color.GRAY

  def addToHistory(ticket: TicketType): MrX = {
    copy(history = ticket :: this.history)
  }

  def removeFromHistory(): MrX = {
    if(history.isEmpty) {
      this
    } else {
      copy(history = history.tail)
    }
  }

  def updateVisibility(isVisible: Boolean): MrX = {
    copy(isVisible = isVisible)
  }

  def updateLastSeen(lastSeen: String): MrX = {
    copy(lastSeen = lastSeen)
  }

  override def setPlayerName(newName: String): Player = {
    if (newName.length < 3) {
      this
    } else if (newName.length > 25) {
      copy(name = newName.substring(0, 25))
    } else {
      copy(name = newName)
    }
  }

  override def setPlayerColor(newColor: String): Player = {
    copy(color = Color.decode(newColor))
  }

  override def setPlayerStation(newStation: Station): Player = {
    copy(station = newStation)
  }

  override def toString(): String = {
    val ticketsString = " - BLACKTICKETS: " + tickets.blackTickets
    if(isVisible) {
      name + " is at " + station.number + ticketsString
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen + ticketsString
    }
  }
}

