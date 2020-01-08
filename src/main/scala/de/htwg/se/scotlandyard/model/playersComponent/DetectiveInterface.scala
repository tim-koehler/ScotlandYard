package de.htwg.se.scotlandyard.model.playersComponent

import java.awt.Color
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station

trait DetectiveInterface {
  var station: Station
  var name: String
  var color: Color
  var taxiTickets: Int
  var busTickets: Int
  var undergroundTickets: Int

  def getPosition(): Station
  def setPlayerName(newName: String): Boolean
}
