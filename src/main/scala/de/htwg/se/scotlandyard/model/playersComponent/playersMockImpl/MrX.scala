package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.playersComponent.MrXInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.{TicketType, Tickets}

class MrX extends MrXInterface{
  override var isVisible: Boolean = _
  override var lastSeen: String = _
  override var history: List[TicketType] = _
  override val lastSeenColor: Color = Color.BLACK

  override def getHistory(): List[TicketType] = List(TicketType.Bus)

  override def addToHistory(ticket: TicketType): Boolean = true

  override def removeFromHistory(): Boolean = true

  override var station: Station = _
  override var name: String = _
  override var color: Color = _
  override var tickets: Tickets = _

  override def setPlayerName(newName: String): Boolean = true
}
