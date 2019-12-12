package de.htwg.se.scotlandyard.model.player

//import java.awt.Color

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station


trait Player {
   var station: Station
   var name: String
   var taxiTickets: Int = 0 // default: 11
   var busTickets: Int = 0 // default: 8
   var undergroundTickets: Int = 0 // default 4
   var color: Color

   def getPosition(): Station = {
      station
   }

   def setPlayerName(newName: String): Boolean = {
      if(newName.length < 3) {
         false
      } else {
         name = newName.substring(0, 3)
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

