package de.htwg.se.scotlandyard.model.playersComponent

import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
import org.scalatest._

import scala.collection.mutable

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      new Station(0, StationType.Taxi)
      val st = new Station(0, StationType.Taxi)
      val mrX = new MrX()
      mrX.station = st

      "have a station number" in {
        mrX.station.number should be (0)
      }
      "have a station Type" in {
        mrX.station.stationType should be (StationType.Taxi)
      }
      "should have a nice String representation when visible" in {
        mrX.isVisible = false
        mrX.toString() shouldNot be (null)
      }
      "should have a nice String representation when hidden" in {
        mrX.isVisible = true
        mrX.toString() shouldNot be (null)
      }
      "getHistory should return history" in {
        mrX.getHistory() should be(mrX.history)
      }
      "addToHistory should add a Ticket to history" in {
        mrX.addToHistory(TicketType.Taxi) should be(true)
      }
      "removeFromHistory should remove Ticket" in {
        mrX.removeFromHistory() should be(true)
        mrX.history = mutable.Stack()
        mrX.removeFromHistory() should be(false)
      }
      "should set name with setPlayerNamer method" in {
        mrX.setPlayerName("Olaf") should be(true)
      }
    }
  }
}
