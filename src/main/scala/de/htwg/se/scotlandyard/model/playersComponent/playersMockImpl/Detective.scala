package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.Tickets

class Detective extends DetectiveInterface{
  override var station: Station = _
  override var name: String = _
  override var color: Color = _
  override var tickets: Tickets = _

  override def setPlayerName(newName: String): Boolean = true
}
