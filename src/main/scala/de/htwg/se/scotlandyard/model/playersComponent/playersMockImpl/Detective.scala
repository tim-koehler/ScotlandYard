package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}

import java.awt.{Color, Point}
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.StationType.StationType

class Detective extends DetectiveInterface {
  override var station: Station = new Station(20, StationType.Taxi)
  override var name: String = "Dt1"
  override var color: Color = Color.GREEN
  override var tickets: Tickets = Tickets(11, 8, 4)

  override def setPlayerName(newName: String): Boolean = true
  override def setPlayerColor(newColor: String): Color = Color.BLUE
}
