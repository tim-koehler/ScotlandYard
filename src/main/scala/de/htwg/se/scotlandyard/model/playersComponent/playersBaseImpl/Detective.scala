package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

import scala.swing.Color

class Detective @Inject() () extends DetectiveInterface {
   override var station: Station = _
   override var name: String = _
   override var color: Color = _
   override var taxiTickets: Int = _ // default: 11
   override var busTickets: Int = _ // default: 8
   override var undergroundTickets: Int = _ // default 4*/

   def setPlayerName(newName: String): Boolean = {
      if(newName.length < 3) {
         false
      } else if (newName.length > 25) {
         name = newName.substring(0, 25)
         true
      } else {
        name = newName
         true
      }
   }

   override def toString(): String = {
      val stationString = station.number + " (" + station.sType.toString.toUpperCase + ")"
      val taxiString = "T: " + taxiTickets
      val busString = ", B: " + busTickets
      val undergroundString = ", U: " + undergroundTickets
      name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
   }
}

