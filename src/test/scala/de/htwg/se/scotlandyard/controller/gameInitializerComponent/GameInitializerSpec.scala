package de.htwg.se.scotlandyard.controller.gameInitializerComponent

import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerBaseImpl.GameInitializer
import de.htwg.se.scotlandyard.model.{Station, StationType, Tickets}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import java.awt.{Color, Point}
import scala.io.Source

class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester{

  val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString

  "GameInitializer" when {
    val gameInitializer = new GameInitializer()
    "initialize" should {
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
    }
    "initDetectiveFromLoad" should {
      "initialize a detective" in {
        val station1 = Station(number = 0, stationType = StationType.Taxi, neighbourTaxis = Set(), neighbourBuses = Set(), neighbourUndergrounds = Set(), tuiCoordinates = new Point(23, 34), guiCoordinates = new Point(3, 4))
        val station2 = Station(number = 1, stationType = StationType.Bus, neighbourTaxis = Set(), neighbourBuses = Set(), neighbourUndergrounds = Set(), tuiCoordinates = new Point(23, 34), guiCoordinates = new Point(3, 4))
        val detective = gameInitializer.initDetectiveFromLoad("Dt1", 1, Tickets(11, 8, 3),  Color.GREEN, Vector(station1, station2))
        detective.name should be ("Dt1")
        detective.station.number should be (1)
        detective.color should be (Color.GREEN)
      }
    }
  }

}
