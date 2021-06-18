package de.htwg.se.scotlandyard.gameinitializer

import akka.http.scaladsl.client.RequestBuilding.{Get, WithTransformation}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}


class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester with ScalatestRouteTest{

  "GameInitializer" when {
    val gameInitializer = new GameInitializer()
    "initialize" should {
      "return an initialized gameModel" in {
        val persistenceGameModel = gameInitializer.initialize(3)
        persistenceGameModel.players(0).name should be ("MrX")
        persistenceGameModel.players(1).name should be ("Dt1")
      }
    }
    "getColorList" should {
      "return color list" in {
        gameInitializer.getColorList()(0) should be(gameInitializer.MRX_COLOR)
        gameInitializer.getColorList()(1) should be(gameInitializer.DT1_COLOR)
      }
    }
  }

  "Rest" when {
    val response = "{\"allPlayerStuck\":false,\"gameRunning\":false,\"mrxVisibleRounds\":[3,8,13,18,24],\"players\":[{\"color\":\"#000000\",\"history\":[],\"isVisible\":false,\"lastSeen\":\"never\",\"name\":\"MrX\",\"playerType\":\"mrx\",\"station\":"
    val stations: Vector[Station] = Vector(Station(1, StationType.Taxi, blackStation = false, Set(2)))
    val gameInitializer = new GameInitializer()
    Rest.route = Rest.createRoutes(gameInitializer, stations);
    "initialize rest endpoint called" should {
      "return an initialized gameModel" in {
        Get("/initialize?nPlayer=3") ~> Rest.route ~> check {
          responseAs[String] should include(response)
        }
      }
    }
    "health endpoint called" should {
      "return an Alive" in {
        Get("/health") ~> Rest.route ~> check {
          responseAs[String] should equal("Alive")
        }
      }
    }
  }
}
