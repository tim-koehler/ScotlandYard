package de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl

import java.awt.Color

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.{Station, StationFactory}

class Detective @Inject() extends DetectiveInterface{
  override var station: Station = StationFactory.createZeroIndexStation()
  override var name: String = _
  override var color: Color = _
  override var taxiTickets: Int = _
  override var busTickets: Int = _
  override var undergroundTickets: Int = _

  override def getPosition(): Station = station

  override def setPlayerName(newName: String): Boolean = true
}
