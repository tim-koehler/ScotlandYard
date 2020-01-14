package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

class Detective(var station: Station, var name: String, var color: Color, var taxiTickets: Int = 11, var busTickets: Int = 8, var undergroundTickets: Int = 4) extends DetectiveInterface {

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
      val stationString = station.number + " (" + station.sType.toString.toUpperCase + ")"
      val taxiString = "T: " + taxiTickets
      val busString = ", B: " + busTickets
      val undergroundString = ", U: " + undergroundTickets
      name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
   }
}

