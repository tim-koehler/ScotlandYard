package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl


import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.util.Tickets

import scala.swing.Color

class Detective @Inject() extends DetectiveInterface{
  override var station: Station = StationFactory.createZeroIndexStation()
  override var name: String = _
  override var color: Color = _
  override var tickets: Tickets = _

  override def setPlayerName(newName: String): Boolean = true
}
