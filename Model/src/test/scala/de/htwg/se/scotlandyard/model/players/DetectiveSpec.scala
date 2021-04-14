package de.htwg.se.scotlandyard.model.players


import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest.{Matchers, WordSpec}

import java.awt.Color


class DetectiveSpec extends WordSpec with Matchers {
  "A Detective" when {
    var detective = Detective()
    "created" should {
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

      "have a nice to String" in {
        detective.toString() should (include("DtX") and include("TICKETS->"))
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
    "setName is called" should {
      "not set too short name" in {
        detective.setPlayerName(detective, "g").name should be("DtX")
      }
      "not set to long name" in {
        detective.setPlayerName(detective, "qwertzuiopüasdfghjklöäyxcvbnmdfsdfdsfsdgfdgfd").name should be("qwertzuiopüasdfghjklöäyxc")
      }
      "set name" in {
        detective.setPlayerName(detective, "qwertzuiopüasdfghjklöäyxc").name should be("qwertzuiopüasdfghjklöäyxc") //30 characters
      }
    }
  }
}
