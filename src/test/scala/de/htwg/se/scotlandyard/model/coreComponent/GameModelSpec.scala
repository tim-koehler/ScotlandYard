package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.{GameModel, TicketType}
import de.htwg.se.scotlandyard.model.GameModel.{WINNING_ROUND, players, round}
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.playersComponent.playersMockImpl.MrX
import org.scalatest._

class GameModelSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameMaster Object" when {
    val gameInitializer = new GameInitializer
    "initialize() is called should" should {
      "return true" in {
        gameInitializer.initialize(2) should be (true)
      }
      "and the current player should be MrX" in {
        GameModel.getCurrentPlayer().isInstanceOf[MrX]
      }
      "and the current player Index should be 0" in {
        GameModel.round = 1
        GameModel.getCurrentPlayerIndex() shouldBe(0)
      }
      "tickets counts" in {
        val currentTaxiTickets = GameModel.getCurrentPlayer().tickets.taxiTickets
        GameModel.increaseTickets(TicketType.Taxi) should be(currentTaxiTickets + 1)

        val currentBusTickets = GameModel.getCurrentPlayer().tickets.busTickets
        GameModel.increaseTickets(TicketType.Bus) should be(currentBusTickets + 1)

        val currentUndergroundTickets = GameModel.getCurrentPlayer().tickets.undergroundTickets
        GameModel.increaseTickets(TicketType.Underground) should be(currentUndergroundTickets + 1)

        GameModel.decreaseTickets(TicketType.Taxi) should be(currentTaxiTickets)
        GameModel.decreaseTickets(TicketType.Bus) should be(currentBusTickets)
        GameModel.decreaseTickets(TicketType.Underground) should be(currentUndergroundTickets)
      }
    }
  }
}