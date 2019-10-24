package de.htwg.se.scotlandyard.model.player

import de.htwg.se.scotlandyard.model.map.Station

trait Player {

   var busTickets = List()
   var taxiTicktes = List()
   var undergroundTickets = List()

   def GetPosition(): Station

}

