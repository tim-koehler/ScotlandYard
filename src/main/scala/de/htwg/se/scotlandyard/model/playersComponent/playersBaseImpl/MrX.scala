package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import java.awt.Color

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class MrX @Inject() extends DetectiveInterface with MrXInterface
{
  override var station: Station = _
  override var name: String = "MrX"
  override var color: Color = Color.BLACK
  override var taxiTickets: Int = 99
  override var busTickets: Int = 99
  override var undergroundTickets: Int = 99
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

  // TODO: Cleanup
  override def getPosition(): Station = station

  override def setPlayerName(newName: String): Boolean = {
    name = newName
    true
  }
}

