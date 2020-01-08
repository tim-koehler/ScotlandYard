package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

//import java.awt.Color

import java.awt.Color

import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import javax.inject.Inject


class Detective @Inject() () extends DetectiveInterface {
   override var station: Station = _
   override var name: String = _
   override var color: Color = _
   override var taxiTickets: Int = _ // default: 11
   override var busTickets: Int = _ // default: 8
   override var undergroundTickets: Int = _ // default 4

   def getPosition(): Station = {
      station
   }

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
      var stationString = station.number + " (" + station.sType.toString.toUpperCase + ")"
      var taxiString = "T: " + taxiTickets
      var busString = ", B: " + busTickets
      var undergroundString = ", U: " + undergroundTickets
      name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
   }
}

