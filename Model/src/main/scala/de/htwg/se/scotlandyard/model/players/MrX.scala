package de.htwg.se.scotlandyard.model.players

import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.Tickets

import java.awt.Color

case class MrX(override val station: Int = 0,
               override val tickets: Tickets = Tickets(99, 99, 99, 5),
               override val name: String = "MrX",
               override val color: Color = Color.BLACK,
               isVisible: Boolean = false,
               lastSeen: String = "never",
               history: List[TicketType] = List()) extends Player(station,name,color,tickets, playerType = Some(PlayerTypes.MRX))
{
  val lastSeenColor: Color = Color.GRAY

  def addToHistory(mrX: MrX, ticket: TicketType): MrX = {
    mrX.copy(history = ticket :: this.history)
  }

  def removeFromHistory(mrX: MrX): MrX = {
    if(history.isEmpty) {
      mrX
    } else {
      mrX.copy(history = history.tail)
    }
  }

  def updateVisibility(mrX: MrX, isVisible: Boolean): MrX = {
    mrX.copy(isVisible = isVisible)
  }

  def updateLastSeen(mrX: MrX, lastSeen: String): MrX = {
    mrX.copy(lastSeen = lastSeen)
  }

  override def setPlayerName(player: Player, newName: String): Player = {
    player.asInstanceOf[MrX]
  }

  override def setPlayerColor(player: Player, newColor: String): Player = {
    player.asInstanceOf[MrX]
  }

  override def setPlayerColor(player: Player, newColor: Color): Player = {
    player.asInstanceOf[MrX]
  }


  override def setPlayerStation(player: Player, newStation: Int): Player = {
    player.asInstanceOf[MrX].copy(station = newStation)
  }

  override def setPlayerTickets(player: Player, newTickets: Tickets): Player = {
    player.asInstanceOf[MrX].copy(tickets = newTickets)
  }

  override def toString(): String = {
    val ticketsString = " - BLACKTICKETS: " + tickets.blackTickets
    if(isVisible) {
      name + " is at " + station + ticketsString
    } else {
      name + " (hidden) was" + " Last seen: " + lastSeen + ticketsString
    }
  }
}

