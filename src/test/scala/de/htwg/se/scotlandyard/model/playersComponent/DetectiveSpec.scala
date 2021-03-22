package de.htwg.se.scotlandyard.model.playersComponent


import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import org.scalatest._


class DetectiveSpec extends WordSpec with Matchers {
  "A Detective" when {
    "new" should {
      val detective = new Detective()
      detective.tickets = Tickets(10, 5, 3)
      detective.station = new Station(0, StationType.Taxi)
      "have a name" in {
        detective.setPlayerName("Bobbie") should be(true)
        detective.setPlayerName("g") should be(false)
        detective.setPlayerName("qwertzuiopüasdfghjklöäyxcvbnmd") should be(true) //30 characters
      }
      "have a nice String representation" in {
        detective.toString() should not be(null)
      }
    }
  }
}
