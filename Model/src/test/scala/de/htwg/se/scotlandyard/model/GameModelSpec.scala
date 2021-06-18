package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class GameModelSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameModel" when {
    val st1 = Station(1, StationType.Taxi, blackStation = false, Set(2))
    val st2 = Station(2, StationType.Bus, blackStation = false, Set(1))
    val gameModel = GameModel(stations = Vector(st1, st2, Station(3, StationType.Underground)), players = Vector(MrX(1), Detective(name = "Dt1"), Detective(name = "Dt2")), gameRunning = true)

    "getCurrentPlayer is called" should {
      "return MrX" in {
        gameModel.getCurrentPlayer(gameModel.players, gameModel.round).name should be("MrX")
      }
    }
    "getPreviousPlayer is called" should {
      "return Last Detective" in {
        gameModel.getPreviousPlayer(gameModel.players, 2).name should be("MrX")
      }
      "return MrX" in {
        gameModel.getPreviousPlayer(gameModel.players, 4).name should be("Dt2")
      }
    }
    "getMrX is called" should {
      "return MrX" in {
        gameModel.getMrX(gameModel.players).name should be("MrX")
      }
    }
    "getDetectives is called" should {
      "return all detectives" in {
        gameModel.getDetectives(gameModel.players).length should be(2)
        gameModel.getDetectives(gameModel.players)(1).name should be("Dt2")
      }
    }
    "getCurrentPlayer is called" should {
      "return 0 in round 1" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 1) should be(0)
      }
      "return also 0 in round 4" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 1) should be(0)
      }
      "return 1 in round 2" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 2) should be(1)
      }
    }
    "setAllPlayerStuck is called" should {
      "return a gamemodel with all player stuck" in {
        gameModel.setAllPlayersStuck(gameModel).allPlayerStuck should be (true)
      }
    }
    "winGame is called" should {
      "return a gamemodel with all player stuck" in {
        gameModel.winGame(gameModel, gameModel.players(0)).win should be (true)
        gameModel.winGame(gameModel, gameModel.players(0)).gameRunning should be (false)
      }
    }
    "updateTickets is called" should {
      def decrementValue(x: Int): Int = {x - 1}
      "return a gamemodel with updated Taxi Tickets" in {
        gameModel.updateTickets(gameModel, TicketType.Taxi)(decrementValue).getCurrentPlayer(gameModel.players,gameModel.round).tickets.taxiTickets should be(99)
      }
      "return a gamemodel with updated Bus Tickets" in {
        gameModel.updateTickets(gameModel, TicketType.Bus)(decrementValue).getCurrentPlayer(gameModel.players,gameModel.round).tickets.taxiTickets should be(99)
      }
      "return a gamemodel with updated Underground Tickets" in {
        gameModel.updateTickets(gameModel, TicketType.Underground)(decrementValue).getCurrentPlayer(gameModel.players,gameModel.round).tickets.taxiTickets should be(99)
      }
      "return a gamemodel with updated Black Tickets" in {
        gameModel.updateTickets(gameModel, TicketType.Black)(decrementValue).getCurrentPlayer(gameModel.players,gameModel.round).tickets.taxiTickets should be(99)
      }
    }
    "updatePlayerPosition is called" should {
      "return a gamemodel with updated Player Position" in {
        gameModel.updatePlayerPosition(gameModel, 1).getCurrentPlayer(gameModel.players,gameModel.round).station should be(1)
      }
    }
    "updateRound is called" should {
      def decrementValue(x: Int): Int = {x + 1}
      "return a gamemodel with updated Round" in {
        gameModel.updateRound(gameModel, decrementValue).round should be(2)
      }
    }
  }
}
