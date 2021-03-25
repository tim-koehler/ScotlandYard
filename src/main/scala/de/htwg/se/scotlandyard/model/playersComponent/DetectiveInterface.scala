package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.{Station, Tickets}

import java.awt.Color

trait DetectiveInterface {
  var station: Station
  var name: String
  var color: Color
  var tickets: Tickets

  def setPlayerName(newName: String): Boolean
  def setPlayerColor(newColor: String): Color
}
