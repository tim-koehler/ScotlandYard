package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.{Color, Point}

import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.StationType.StationType
import de.htwg.se.scotlandyard.util.TicketType.TicketType
import de.htwg.se.scotlandyard.util.{StationType, TicketType, Tickets}

class MrX extends MrXInterface with DetectiveInterface {
  override var isVisible: Boolean = true
  override var lastSeen: String = "never"
  override var history: List[TicketType] = List(TicketType.Taxi)
  override val lastSeenColor: Color = Color.GRAY

  override def getHistory(): List[TicketType] = List(TicketType.Bus)

  override def addToHistory(ticket: TicketType): Boolean = true

  override def removeFromHistory(): Boolean = true

  override var station: Station = new Station {
    override val number: Integer = 10
    override var sType: StationType = StationType.Bus
    override var neighbourTaxis: Set[Station] = _
    override var neighbourBuses: Set[Station] = _
    override var neighbourUndergrounds: Set[Station] = _
    override var tuiCoords: (Integer, Integer) = _
    override var guiCoords: Point = _
  }
  override var name: String = "MrX"
  override var color: Color = Color.BLACK
  override var tickets: Tickets = Tickets(99, 99, 99, 5)

  override def setPlayerName(newName: String): Boolean = true
  override def setPlayerColor(newColor: String): Boolean = true
}
