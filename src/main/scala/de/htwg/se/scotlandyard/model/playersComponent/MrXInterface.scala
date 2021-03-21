package de.htwg.se.scotlandyard.model.playersComponent

import java.awt.Color

import de.htwg.se.scotlandyard.model.TicketType.TicketType

import scala.collection.mutable

trait MrXInterface extends DetectiveInterface {
  var isVisible: Boolean
  var lastSeen: String
  var history: mutable.Stack[TicketType]
  val lastSeenColor: Color

  def getHistory(): mutable.Stack[TicketType]
  def addToHistory(ticket: TicketType): Boolean
  def removeFromHistory(): Boolean
}
