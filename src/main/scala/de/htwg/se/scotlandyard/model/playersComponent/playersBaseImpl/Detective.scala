package de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl

import com.google.inject.Inject
import de.htwg.se.scotlandyard.model.{Station, Tickets}
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

import java.awt.Color

class Detective @Inject() extends DetectiveInterface {
   override var station: Station = _
   override var name: String = _
   override var color: Color = _
   override var tickets: Tickets = _

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

   def setPlayerColor(newColor: String): Color = {
      val oldColor = this.color
      this.color = Color.decode(newColor)
      oldColor
   }

   override def toString(): String = {
      val stationString = station.number + " (" + station.stationType.toString.toUpperCase + ")"
      val taxiString = "T: " + tickets.taxiTickets
      val busString = ", B: " + tickets.busTickets
      val undergroundString = ", U: " + tickets.undergroundTickets
      name + ": " + stationString + "  TICKETS-> " + taxiString + busString + undergroundString
   }
}

