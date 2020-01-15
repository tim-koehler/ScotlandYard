package de.htwg.se.scotlandyard.model.playersComponent

import java.awt.Color

import de.htwg.se.scotlandyard.util.TicketType.TicketType

trait MrXInterface extends DetectiveInterface {
  var isVisible: Boolean
  var lastSeen: String
  var history: List[TicketType]
  val lastSeenColor: Color

  def getHistory(): List[TicketType]
  def addToHistory(ticket: TicketType): Boolean
  def removeFromHistory(): Boolean
}
