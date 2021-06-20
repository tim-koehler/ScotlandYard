package de.htwg.se.scotlandyard.model.players

import de.htwg.se.scotlandyard.model.Tickets
import de.htwg.se.scotlandyard.model.players.PlayerTypes.PlayerTypes

import java.awt.Color

abstract class Player(
                       val station: Int = 0,
                       val name: String = "Dtx",
                       val color: Color = Color.GRAY,
                       val tickets: Tickets = Tickets(),
                       val playerType: Option[PlayerTypes] = Some(PlayerTypes.DETECTIVE)
                     ) {
  def setPlayerName(player: Player, newName: String): Player
  def setPlayerColor(player: Player, newColor: String): Player
  def setPlayerColor(player: Player, newColor: Color): Player
  def setPlayerStation(player: Player, newStation: Int): Player
  def setPlayerTickets(player: Player, newTickets: Tickets): Player
}
