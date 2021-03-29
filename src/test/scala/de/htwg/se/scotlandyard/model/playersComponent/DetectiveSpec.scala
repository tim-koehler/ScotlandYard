package de.htwg.se.scotlandyard.model.playersComponent


import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest._

import java.awt.Color


class DetectiveSpec extends WordSpec with Matchers {
  "A Detective" when {
    "new" should {
      var detective = Detective()
      detective = detective.setPlayerTickets(Tickets(10, 5, 3)).asInstanceOf[Detective]
      detective = detective.setPlayerStation(Station(0, StationType.Taxi)).asInstanceOf[Detective]
      "have a name" in {
        detective.setPlayerName("Bobbie") should be(true)
        detective.setPlayerName("g") should be(false)
        detective.setPlayerName("qwertzuiopüasdfghjklöäyxcvbnmd") should be(true) //30 characters
      }
      "have a nice String representation" in {
        detective.toString() should (include("Bobbie") and include("TICKETS->"))
      }
    }
    "setPlayerColor is called" should {
      "return old color" in {
        val detective = Detective()
        detective.setPlayerColor(Color.BLUE)
        detective.setPlayerColor("#ffffff") should be(Color.BLUE)
      }
    }
  }
}
