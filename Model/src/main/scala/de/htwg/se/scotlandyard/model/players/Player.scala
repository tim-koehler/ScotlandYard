package de.htwg.se.scotlandyard.model.players

import de.htwg.se.scotlandyard.model.{Station, Tickets}

import java.awt.Color

abstract class Player(
                       val station: Station = Station(),
                       val name: String = "Dtx",
                       val color: Color = Color.GRAY,
                       val tickets: Tickets = Tickets(),
                     ) {

  def setPlayerName(player: Player, newName: String): Player
  def setPlayerColor(player: Player, newColor: String): Player
  def setPlayerColor(player: Player, newColor: Color): Player
  def setPlayerStation(player: Player, newStation: Station): Player
  def setPlayerTickets(player: Player, newTickets: Tickets): Player
}
