package de.htwg.se.scotlandyard.model.players

import de.htwg.se.scotlandyard.model.{Station, Tickets}

import java.awt.Color

case class Detective(override val station: Station = Station(),
                     override val name: String = "DtX",
                     override val color: Color = Color.LIGHT_GRAY,
                     isStuck: Boolean = false,
                     override val tickets: Tickets = Tickets()) extends Player(station, name, color, tickets, playerType = Some(PlayerTypes.DETECTIVE)) {

  override def setPlayerName(player: Player, newName: String): Player = {
    val detective = player.asInstanceOf[Detective]
    if (newName.length < 3 || newName.equals("MrX")) {
      detective
    } else if (newName.length > 25) {
      detective.copy(name = newName.substring(0, 25))
    } else {
      detective.copy(name = newName)
    }
  }

  override def setPlayerColor(player: Player, newColor: String): Player = {
    this.setPlayerColor(player, Color.decode(newColor))
  }

  override def setPlayerColor(player: Player, newColor: Color): Player = {
    player.asInstanceOf[Detective].copy(color = newColor)
  }

  override def setPlayerStation(player: Player, newStation: Station): Player = {
    player.asInstanceOf[Detective].copy(station = newStation)
  }

  override def setPlayerTickets(player: Player, newTickets: Tickets): Player = {
    player.asInstanceOf[Detective].copy(tickets = newTickets)
  }

  override def toString(): String = {
    val stationString = station.number + " (" + station.stationType.toString.toUpperCase + ")"
    val taxiString = "T: " + tickets.taxiTickets
    val busString = ", B: " + tickets.busTickets
    val undergroundString = ", U: " + tickets.undergroundTickets
    name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
  }
}
