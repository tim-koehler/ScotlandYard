package de.htwg.se.scotlandyard.model.players


import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest._

import java.awt.Color


class DetectiveSpec extends WordSpec with Matchers {
  "A Detective" when {
    "should" should {
      var detective = Detective()
      detective = detective.setPlayerTickets(detective, Tickets(10, 5, 3)).asInstanceOf[Detective]
      "have correct tickets" in {
        detective.tickets.taxiTickets should be (10)
        detective.tickets.busTickets should be (5)
        detective.tickets.undergroundTickets should be (3)
      }

      detective = detective.setPlayerStation(detective, Station(1, StationType.Taxi)).asInstanceOf[Detective]
      "have the correct station" in {
        detective.station.number should be (1)
      }

      "have a name" in {
        detective = detective.setPlayerName(detective, "Bobbie").asInstanceOf[Detective]
        detective.name should be("Bobbie")
        detective.toString() should (include("Bobbie") and include("TICKETS->"))
        //detective.setPlayerName("g") should be(false)
        //detective.setPlayerName("qwertzuiopüasdfghjklöäyxcvbnmd") should be(true) //30 characters
      }
    }
    "setPlayerColor is called" should {
      "return old color" in {
        var detective = Detective()
        detective.setPlayerColor(detective, Color.BLUE).color should be (Color.BLUE)
        detective = detective.setPlayerColor(detective, Color.BLUE).asInstanceOf[Detective]
        detective.setPlayerColor(detective, "#ffffff").color should be(Color.WHITE)
      }
    }
  }
}
