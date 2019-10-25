package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.map.StationType.StationType
import de.htwg.se.scotlandyard.model.player.Player

case class Station(number: Integer, sType: StationType) {
  val currentPlayer: Player
}