package de.htwg.se.scotlandyard.model.player

import de.htwg.se.scotlandyard.model.map.Station

trait Player {
   var taxiTickets: Int = 11 // default: 11
   var busTickets: Int = 8 // default: 8
   var undergroundTickets: Int = 4 // default 4

   def getPosition(): Station
}

