package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster.{players, round, winningRound}
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
import de.htwg.se.scotlandyard.util.TicketType
import org.scalatest._

class GameMasterSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameMaster Object" when {
    val controller = new Controller
    "startGame() is called should" should {
      "return true" in {
        GameInitializer.initialize()
        GameInitializer.initPlayers(2)
        GameMaster.startGame() should be (true)
      }
      "and the current player should be MrX" in {
        GameMaster.getCurrentPlayer().isInstanceOf[MrX]
      }
      "and the current player Index should be 0" in {
        GameMaster.round = 1
        GameMaster.getCurrentPlayerIndex() shouldBe(0)
      }
      "and the next round should be 2" in {
        GameMaster.round = 1
        GameMaster.nextRound() shouldBe(2)
        GameMaster.totalRound shouldBe(1)
      }
      "and MrX should also be hidden" in {
        GameMaster.checkMrXVisibility() shouldBe (false)

        val rounds = GameMaster.totalRound
        GameMaster.totalRound = 1
        GameMaster.checkMrXVisibility() shouldBe (false)
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
      "and target Station should be empty" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationEmpty"))(199) should be(true)

        GameMaster.getCurrentPlayer().station = GameMaster.stations(3)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationEmpty"))(3) should be(false)
      }
      "and Taxi move should be valid" in {
        GameMaster.getCurrentPlayer().station = GameMaster.stations(153)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(152) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(183) should be(false)

        val tickets = GameMaster.getCurrentPlayer().taxiTickets
        GameMaster.getCurrentPlayer().taxiTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTaxiMoveValid"))(2) should be(false)
        GameMaster.getCurrentPlayer().taxiTickets = tickets
      }
      "and Bus move should be valid" in {
        GameMaster.getCurrentPlayer().station = GameMaster.stations(153)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(124) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(138) should be(false)

        val tickets = GameMaster.getCurrentPlayer().busTickets
        GameMaster.getCurrentPlayer().busTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isBusMoveValid"))(2) should be(false)
        GameMaster.getCurrentPlayer().busTickets = tickets
      }
      "and Underground move should be valid" in {
        GameMaster.getCurrentPlayer().station = GameMaster.stations(153)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(140) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(124) should be(false)

        val tickets = GameMaster.getCurrentPlayer().undergroundTickets
        GameMaster.getCurrentPlayer().undergroundTickets = 0
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isUndergroundMoveValid"))(3) should be(false)
        GameMaster.getCurrentPlayer().undergroundTickets = tickets
      }
      "and target Station is in Bounds" in {
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1) should be(true)
        GameMaster invokePrivate PrivateMethod[Boolean](Symbol("isTargetStationInBounds"))(1000) should be(false)
      }
      "and validateMove() should return true" in {
        GameMaster.getCurrentPlayer().station = GameMaster.stations(153)
        GameMaster.players(0).station = GameMaster.stations(180)

        GameMaster.validateMove(166, TicketType.Taxi) should be(true)
        GameMaster.validateMove(165, TicketType.Bus) should be(false)
        GameMaster.validateMove(1, TicketType.Underground) should be(false)

        GameMaster.players(0).station = GameMaster.stations(55)
        GameMaster.getCurrentPlayer().station = GameMaster.stations(89)

        GameMaster.validateMove(71, TicketType.Taxi) should be(true)
        GameMaster.validateMove(128, TicketType.Underground) should be(true)

        GameMaster.getCurrentPlayer().station = GameMaster.stations(89)
        GameMaster.validateMove(105, TicketType.Bus) should be(true)
        GameMaster.validateMove(90, TicketType.Taxi) should be(false)

        GameMaster.getCurrentPlayer().taxiTickets = 0;
        GameMaster.getCurrentPlayer().busTickets= 0;
        GameMaster.getCurrentPlayer().undergroundTickets = 0;

        GameMaster.validateMove(128, TicketType.Underground) should be(false)
        GameMaster.validateMove(55, TicketType.Bus) should be(false)
        GameMaster.validateMove(88, TicketType.Taxi) should be(false)
      }
      "mrx should win in round 24" in {
        GameMaster.round = winningRound * players.length
        GameMaster.checkMrXWin() shouldBe(true)
      }
    }
  }
}