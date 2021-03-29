package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.{Station, Tickets}

import java.awt.Color

abstract class Player(
                       val station: Station = Station(),
                       val name: String = "Dtx",
                       val color: Color = Color.GRAY,
                       val tickets: Tickets = Tickets(),
                     ) {

  def setPlayerStation(newStation: Station): Player
  def setPlayerName(newName: String): Player
  def setPlayerColor(newColor: String): Player
  def setPlayerColor(newColor: Color): Player
  def setPlayerTickets(newTickets: Tickets): Player
}
