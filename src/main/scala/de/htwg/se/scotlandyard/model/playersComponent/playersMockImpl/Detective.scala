package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.{Color, Point}

import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.util.StationType.StationType
import de.htwg.se.scotlandyard.util.{StationType, Tickets}

class Detective extends DetectiveInterface {
  override var station: Station = new Station {
    override val number: Integer = 20
    override var sType: StationType = StationType.Taxi
    override var neighbourTaxis: Set[Station] = _
    override var neighbourBuses: Set[Station] = _
    override var neighbourUndergrounds: Set[Station] = _
    override var tuiCoords: (Integer, Integer) = _
    override var guiCoords: Point = _
  }
  override var name: String = "Dt1"
  override var color: Color = Color.GREEN
  override var tickets: Tickets = Tickets(11, 8, 4)

  override def setPlayerName(newName: String): Boolean = true
  override def setPlayerColor(newColor: String): Boolean = true
}
