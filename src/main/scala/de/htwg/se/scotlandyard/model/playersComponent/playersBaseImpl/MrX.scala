package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import java.awt.Color

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Tickets

import scala.collection.mutable

class MrX @Inject() extends DetectiveInterface with MrXInterface
{
  override var station: Station = _
  override var name: String = "MrX"
  override var color: Color = Color.BLACK
  override var tickets: Tickets = Tickets(99, 99, 99, 5)
  override var isVisible: Boolean = false
  override var lastSeen: String = "never"
  override var history: mutable.Stack[TicketType] = mutable.Stack()
  override val lastSeenColor: Color = Color.GRAY


  override def toString(): String = {
    val ticketsString = " - BLACKTICKETS: " + tickets.blackTickets
    if(isVisible) {
      name + " is at " + station.number + ticketsString
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen + ticketsString
    }
  }

  def getHistory(): mutable.Stack[TicketType] = {
    this.history
  }

  def addToHistory(ticket: TicketType): Boolean = {
    this.history.push(ticket)
    true
  }

  def removeFromHistory(): Boolean = {
    if(history.isEmpty) {
      false
    } else {
      history.pop()
      true
    }
  }

  override def setPlayerName(newName: String): Boolean = {
    name = newName
    true
  }

  override def setPlayerColor(newColor: String): Boolean = {
    true
  }
}

