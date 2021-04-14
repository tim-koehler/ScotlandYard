package de.htwg.se.scotlandyard.model.players

import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType}
import org.scalatest.{Matchers, WordSpec}

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "created" should {
      var mrX = MrX(station = Station(1, StationType.Underground))

      "have a station number" in {
        mrX.station.number should be (1)
      }
      "have a station Type" in {
        mrX.station.stationType should be (StationType.Underground)
      }
      "have a nice String representation when visible" in {
        mrX = MrX(isVisible = true)
        mrX.toString() should (include("BLACKTICKETS") and include("MrX") and include("is at"))
      }
      "have a nice String representation when hidden" in {
        mrX = MrX()
        mrX.toString() should (include("BLACKTICKETS") and include("MrX") and include("(hidden) was"))
      }
      "history should return history" in {
        mrX = MrX()
        mrX.history.isEmpty should be(true)
        mrX = MrX(history = List(TicketType.Taxi, TicketType.Underground, TicketType.Bus))
        mrX.history(1) should be (TicketType.Underground)
      }
      "update last seen" in {
        mrX.updateLastSeen(mrX, "newLastSeen").lastSeen should be ("newLastSeen")
      }
      "addToHistory should add a Ticket to history" in {
        mrX = MrX()
        mrX.addToHistory(mrX, TicketType.Taxi).history.head should be(TicketType.Taxi)
      }
      "removeFromHistory should remove Ticket" in {
        mrX = MrX(history = List(TicketType.Taxi, TicketType.Underground, TicketType.Bus))
        mrX.removeFromHistory(mrX).history.size should be(2)
      }
      "removeFromHistory should remove nothing when history empty" in {
        mrX = mrX.copy(history = List())
        mrX.removeFromHistory(mrX).history.size should be(0)
      }
    }
  }
}
