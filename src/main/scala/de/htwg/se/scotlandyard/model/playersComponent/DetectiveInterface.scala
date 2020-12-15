package de.htwg.se.scotlandyard.model.playersComponent

import java.awt.Color

import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.Tickets

trait DetectiveInterface {
  var station: Station
  var name: String
  var color: Color
  var tickets: Tickets

  def setPlayerName(newName: String): Boolean
  def setPlayerColor(newColor: String): Boolean
}
