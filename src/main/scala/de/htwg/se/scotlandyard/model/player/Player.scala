package de.htwg.se.scotlandyard.model.player

import de.htwg.se.scotlandyard.model.map.Station

trait Player {
   var station: Station
   var name: String
   var taxiTickets: Int = 0 // default: 11
   var busTickets: Int = 0 // default: 8
   var undergroundTickets: Int = 0 // default 4

   def getPosition(): Station = {
      station
   }

   override def toString(): String = {
      var stationString = station.number + " (" + station.sType.toString.toUpperCase + ")"
      var taxiString = "T: " + taxiTickets
      var busString = ", B: " + busTickets
      var undergroundString = ", U: " + undergroundTickets
      name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
   }
}

