package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.players.MrX
import org.scalatest._

class gameModelSpec extends WordSpec with Matchers with PrivateMethodTester {
  "GameMaster Object" when {
    val gameInitializer = new GameInitializer
    val gameModel = gameInitializer.initialize(3)
    "getCurrentPlayer is called should" should {
      "return MrX" in {
        gameModel.getCurrentPlayer(gameModel.players, 1).isInstanceOf[MrX]
      }
    }
    "getPreviousPlayer is called should" should {
      "return Last Detective" in {
        gameModel.getCurrentPlayer(gameModel.players, 1).name should be("Dt2")
      }
      "return MrX" in {
        gameModel.getCurrentPlayer(gameModel.players, 2).isInstanceOf[MrX] should be(true)
      }
    }
    "getMrX is called should" should {
      "return MrX" in {
        gameModel.getMrX(gameModel.players).isInstanceOf[MrX] should be (true)
      }
    }
    "getDetectives is called should" should {
      "return all detectives" in {
        gameModel.getDetectives(gameModel.players).length should be(2)
        gameModel.getDetectives(gameModel.players)(0).name should be("Dt1")
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
    "updatePlayerPosition is called should" should {
      "return the new Station" in {
        gameModel.updatePlayerPosition(gameModel.getCurrentPlayer(gameModel.players, 1), 5).number should be (5)
      }
    }
  }
}