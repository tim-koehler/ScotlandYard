package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.playersComponent.MrXInterface
import de.htwg.se.scotlandyard.util.TicketType
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import javax.inject.Inject



class MrX @Inject() () extends Detective() with MrXInterface{
  override var isVisible: Boolean = _
  override var lastSeen: String = _
  override var history: List[TicketType] = List(TicketType.Taxi, TicketType.Bus, TicketType.Underground)
  override val lastSeenColor: Color = Color.GRAY

  override def getHistory(): List[TicketType] = history

  override def addToHistory(ticket: TicketType): Boolean = true

  override def removeFromHistory(): Boolean = true
}
