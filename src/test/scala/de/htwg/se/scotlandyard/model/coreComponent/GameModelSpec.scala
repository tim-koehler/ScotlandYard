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
      "and MrX should also be hidden" in {
        GameModel.checkMrXVisibility() shouldBe (false)

        val rounds = GameModel.totalRound
        GameModel.totalRound = 1
        GameModel.checkMrXVisibility() shouldBe (false)
        GameModel.totalRound = 3
        GameModel.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 8
        GameModel.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 13
        GameModel.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 18
        GameModel.checkMrXVisibility() shouldBe (true)
        GameModel.totalRound = 24
        GameModel.checkMrXVisibility() shouldBe (true)

        GameModel.totalRound = rounds
        GameModel.getMrX().lastSeen shouldBe ("never")
      }
      "tickets counts" in {
        val currentTaxiTickets = GameModel.getCurrentPlayer().tickets.taxiTickets
        GameModel.increaseTickets(TicketType.Taxi) should be(currentTaxiTickets + 1)

        val currentBusTickets = GameModel.getCurrentPlayer().tickets.busTickets
        GameModel.increaseTickets(TicketType.Bus) should be(currentBusTickets + 1)

        val currentUndergoundTickets = GameModel.getCurrentPlayer().tickets.undergroundTickets
        GameModel.increaseTickets(TicketType.Underground) should be(currentUndergoundTickets + 1)

        GameModel.decreaseTickets(TicketType.Taxi) should be(currentTaxiTickets)
        GameModel.decreaseTickets(TicketType.Bus) should be(currentBusTickets)
        GameModel.decreaseTickets(TicketType.Underground) should be(currentUndergoundTickets)
      }
    }
  }
}