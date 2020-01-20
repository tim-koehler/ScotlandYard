package de.htwg.se.scotlandyard.model.playersComponent


import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.Detective
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.StationFactory
import de.htwg.se.scotlandyard.util.Tickets
import org.scalatest._


class DetectiveSpec extends WordSpec with Matchers {
  "A Detective" when {
    "new" should {
      val detective = new Detective()
      detective.tickets = Tickets(10, 5, 3)
      detective.station = StationFactory.createZeroIndexStation()
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
