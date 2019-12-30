package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

//import java.awt.Color

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface


class Detective(var station: Station, var name: String, var color: Color) extends DetectiveInterface {
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

