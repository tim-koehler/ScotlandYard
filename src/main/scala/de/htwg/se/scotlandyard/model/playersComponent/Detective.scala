package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.{Station, Tickets}

import java.awt.Color

case class Detective(override val station: Station = Station(),
                     override val name: String = "DtX",
                     override val color: Color = Color.LIGHT_GRAY,
                     override val tickets: Tickets  = Tickets()) extends Player(station, name, color, tickets) {

  override def setPlayerName(newName: String): Player = {
    if (newName.length < 3) {
      this
    } else if (newName.length > 25) {
      copy(name = newName.substring(0, 25))
    } else {
      copy(name = newName)
    }
  }

  override def setPlayerColor(newColor: String): Player = {
    copy(color = Color.decode(newColor))
  }

  override def setPlayerStation(newStation: Station): Player = {
    copy(station = newStation)
  }

  override def setPlayerTickets(newTickets: Tickets): Player = {
    copy(tickets = newTickets)
  }

  override def toString(): String = {
    val stationString = station.number + " (" + station.stationType.toString.toUpperCase + ")"
    val taxiString = "T: " + tickets.taxiTickets
    val busString = ", B: " + tickets.busTickets
    val undergroundString = ", U: " + tickets.undergroundTickets
    name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
  }
}
