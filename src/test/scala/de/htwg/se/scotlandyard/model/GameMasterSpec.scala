package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.Station
import de.htwg.se.scotlandyard.model.player.{MrX, TicketType}
import org.scalatest._

class GameMasterSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameMaster Object" when {
    "startGame() is called should" should {
      "return true" in {
        GameInitializer.initialize()
        GameMaster.startGame() should be (true)
      }
      "and the current player should be MrX" in {
        GameMaster.getCurrentPlayer().isInstanceOf[MrX]
      }
      // TODO: @Roland fix here!
      "and the current player Index should be 0" in {
        GameMaster.round = 1
        GameMaster.getCurrentPlayerIndex() shouldBe(0)
      }
      "and the next round should be 2" in {
        // TODO: @Roland fix here!!
        GameMaster.round = 1
        GameMaster.nextRound() shouldBe(2)
        GameMaster.totalRound shouldBe(1)
      }
      "and MrX should also be hidden" in {
        GameMaster.checkMrXVisibility() shouldBe (false)

        val rounds = GameMaster.totalRound
        GameMaster.totalRound = 3
        GameMaster.checkMrXVisibility() shouldBe (true)
        GameMaster.totalRound = 8
        GameMaster.checkMrXVisibility() shouldBe (true)
        GameMaster.totalRound = 13
        GameMaster.checkMrXVisibility() shouldBe (true)
        GameMaster.totalRound = 18
        GameMaster.checkMrXVisibility() shouldBe (true)
        GameMaster.totalRound = 24
        GameMaster.checkMrXVisibility() shouldBe (true)

        GameMaster.totalRound = rounds
        GameMaster.players(0).asInstanceOf[MrX].lastSeen shouldBe ("never")
      }
      "and updatePlayerPosition() should return new Station" in {
        GameMaster.updatePlayerPosition(2, TicketType.Taxi) should be(GameMaster.stations(2))
        GameMaster.updatePlayerPosition(3, TicketType.Bus) should be(GameMaster.stations(3))
        GameMaster.updatePlayerPosition(2, TicketType.Underground) should be(GameMaster.stations(2))
      }
      "and target Station should be empty" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationEmpty"))(3) should be(true)

        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("setPlayerPosition"))(3)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationEmpty"))(3) should be(false)
      }
      "and Taxi move should be valid" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("setPlayerPosition"))(3)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(1) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(2) should be(false)

        val tickets = GameMaster.getCurrentPlayer().taxiTickets
        GameMaster.getCurrentPlayer().taxiTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(2) should be(false)
        GameMaster.getCurrentPlayer().taxiTickets = tickets
      }
      "and Bus move should be valid" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("setPlayerPosition"))(3)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(2) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(1) should be(false)

        val tickets = GameMaster.getCurrentPlayer().busTickets
        GameMaster.getCurrentPlayer().busTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(1) should be(false)
        GameMaster.getCurrentPlayer().busTickets = tickets
      }
      "and Underground move should be valid" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("setPlayerPosition"))(2)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(3) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(1) should be(false)

        val tickets = GameMaster.getCurrentPlayer().undergroundTickets
        GameMaster.getCurrentPlayer().undergroundTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(2) should be(false)
        GameMaster.getCurrentPlayer().undergroundTickets = tickets
      }
      "and target Station is in Bounds" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1000) should be(false)
      }
      "and validateMove() should return true" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("setPlayerPosition"))(1)
        GameMaster.validateMove(2, TicketType.Taxi) should be(true)
        GameMaster.validateMove(3, TicketType.Bus) should be(false)
        GameMaster.validateMove(2, TicketType.Underground) should be(false)
        GameMaster.validateMove(1, TicketType.Bus) should be(false)

        GameMaster.validateMove(1, TicketType.Underground) should be(false)
        GameMaster.validateMove(2, TicketType.Bus) should be(false)
        GameMaster.validateMove(1, TicketType.Taxi) should be(false)
      }
    }
  }
}