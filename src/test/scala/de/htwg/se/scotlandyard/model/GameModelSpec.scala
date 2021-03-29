package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import org.scalatest._

class GameModelSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameModel" when {
    val st1 = Station(1, StationType.Taxi,  Set(Station(2)))
    val st2 = Station(2, StationType.Bus, Set(Station(1)))
    val gameModel = GameModel(stations = Vector(st1, st2, Station(3, StationType.Underground)), players = Vector(MrX(st1), Detective(name = "Dt1"), Detective(name = "Dt2")), gameRunning = true)

    "getCurrentPlayer is called should" should {
      "return MrX" in {
        gameModel.getCurrentPlayer(gameModel.players, gameModel.round).name should be("MrX")
      }
    }
    "getPreviousPlayer is called should" should {
      "return Last Detective" in {
        gameModel.getCurrentPlayer(gameModel.players, 2).name should be("Dt1")
      }
      "return MrX" in {
        gameModel.getCurrentPlayer(gameModel.players, 4).name should be("MrX")
      }
    }
    /*"getPreviousPlayer" should {
      gameModel.getPreviousPlayer(gameModel.players, 2).name should be("MrX")
    }*/
    "getMrX is called should" should {
      "return MrX" in {
        gameModel.getMrX(gameModel.players).name should be ("MrX")
      }
    }
    "getDetectives is called should" should {
      "return all detectives" in {
        gameModel.getDetectives(gameModel.players).length should be(2)
        gameModel.getDetectives(gameModel.players)(1).name should be("Dt2")
      }
    }
    "getCurrentPlayer is called should" should {
      "return 0 in round 1" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 1) should be (0)
      }
      "return also 0 in round 4" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 1) should be (0)
      }
      "return 1 in round 2" in {
        gameModel.getCurrentPlayerIndex(gameModel.players, 2) should be (1)
      }
    }
    /*"updatePlayerPosition is called should" should {
      "return the new Station" in {
        gameModel.updatePlayerPosition(gameModel, 1).getCurrentPlayer(gameModel.players, gameModel.round).station.number should be (2)
      }
    }*/
  }
}