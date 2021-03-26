package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.{TicketType}
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.MrX
import org.scalatest._

class gameModelSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameMaster Object" when {
    val gameInitializer = new GameInitializer
    val gameModel = gameInitializer.initialize(2)
    "initialize() is called should" should {
      "return true" in {
        gameInitializer.initialize(2).players.length should be (2)
      }
      "and the current player should be MrX" in {
        gameModel.getCurrentPlayer.isInstanceOf[MrX]
      }
      "and the current player Index should be 0" in {
        //gameModel.round = 1
        gameModel.getCurrentPlayerIndex shouldBe 0
      }
      "tickets counts" in {
        val currentTaxiTickets = gameModel.getCurrentPlayer.tickets.taxiTickets
        gameModel.updateTickets(TicketType.Taxi)(gameModel.incrementValue) should be(currentTaxiTickets + 1)

        val currentBusTickets = gameModel.getCurrentPlayer.tickets.busTickets
        gameModel.updateTickets(TicketType.Bus)(gameModel.incrementValue) should be(currentBusTickets + 1)

        val currentUndergroundTickets = gameModel.getCurrentPlayer.tickets.undergroundTickets
        gameModel.updateTickets(TicketType.Underground)(gameModel.incrementValue) should be(currentUndergroundTickets + 1)

        val currentBlackTickets = gameModel.getMrX.tickets.blackTickets
        gameModel.updateTickets(TicketType.Black)(gameModel.incrementValue) should be(currentBlackTickets + 1)

        gameModel.updateTickets(TicketType.Taxi)(gameModel.decrementValue) should be(currentTaxiTickets)
        gameModel.updateTickets(TicketType.Bus)(gameModel.decrementValue) should be(currentBusTickets)
        gameModel.updateTickets(TicketType.Underground)(gameModel.decrementValue) should be(currentUndergroundTickets)
        gameModel.updateTickets(TicketType.Black)(gameModel.decrementValue) should be(currentBlackTickets)
      }
    }
    "getLastPlayer() is called" should {
      gameInitializer.initialize(2)
      "return the last detective if Mrx is current Player" in {
        //gameModel.round = 2
        gameModel.getPreviousPlayer.name should be("MrX")
      }
      "return the last player if a detective is the current player" in {
        //gameModel.round = 1
        gameModel.getPreviousPlayer.name should be("Dt1")
      }
    }
  }
}