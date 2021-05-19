package de.htwg.se.scotlandyard.gameinitializer

import de.htwg.se.scotlandyard.gameinitializer.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import java.awt.{Color, Point}
import scala.io.Source
import scala.util.{Failure, Success, Try}

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester{

  var stationsSource = ""
  Try {
    stationsSource = Source.fromFile("./resources/stations.json").getLines.mkString
  } match {
    case Success(value) =>
    case Failure(e) =>
      stationsSource = Source.fromFile("../resources/stations.json").getLines.mkString
  }

  "GameInitializer" when {
    val gameInitializer = new GameInitializer()
    /*"initialize" should {
      "return an initialized gameModel" in {
        val gameModel = gameInitializer.initialize(3, stationsSource)
        gameModel.stations(89).number should be (89)
        gameModel.players(0).name should be ("MrX")
        gameModel.players(1).name should be ("Dt1")
      }
    }
    "getColorList" should {
      "return color list" in {
        gameInitializer.getColorList()(0) should be(gameInitializer.MRX_COLOR)
        gameInitializer.getColorList()(1) should be(gameInitializer.DT1_COLOR)
      }
    }*/
  }
}
