package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType, Tickets}

import java.awt.{Color, Point}
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.StationType.StationType
import de.htwg.se.scotlandyard.model.TicketType.TicketType

import scala.collection.mutable

class MrX extends MrXInterface with DetectiveInterface {
  override var isVisible: Boolean = true
  override var lastSeen: String = "never"
  override var history: mutable.Stack[TicketType] = mutable.Stack(TicketType.Taxi)
  override val lastSeenColor: Color = Color.GRAY

  override def getHistory(): mutable.Stack[TicketType] = mutable.Stack(TicketType.Bus)

  override def addToHistory(ticket: TicketType): Boolean = true

  override def removeFromHistory(): Boolean = true

  override var station: Station = new Station(10, StationType.Bus)
  override var name: String = "MrX"
  override var color: Color = Color.BLACK
  override var tickets: Tickets = Tickets(99, 99, 99, 5)

  override def setPlayerName(newName: String): Boolean = true
  override def setPlayerColor(newColor: String): Boolean = true
}
