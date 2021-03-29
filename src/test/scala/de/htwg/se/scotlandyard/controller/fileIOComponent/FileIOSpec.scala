package de.htwg.se.scotlandyard.controller.fileIOComponent

import de.htwg.se.scotlandyard.ScotlandYard.stationsJsonFilePath
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import scala.io.Source

class FileIOSpec extends WordSpec with Matchers with PrivateMethodTester {
  val stationsSource: String = Source.fromFile(stationsJsonFilePath).getLines.mkString
  "FileIO" when {
    "new" should {
      val gameInitializer = new GameInitializer();
      val gameModel = gameInitializer.initialize(3, stationsSource)

      val jsonFileIO = new de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOJsonImpl.FileIO(gameInitializer)
      val xmlFileIO = new de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOXmlImpl.FileIO(gameInitializer)

      jsonFileIO.pathname = "ScotlandYard_test.json"
      xmlFileIO.pathname = "ScotlandYard_test.xml"

      "json load and save" in {
        jsonFileIO.save(gameModel, gameModel.getMrX(gameModel.players)) should be(true)
        jsonFileIO.load().round should be(1)
      }
      "xml load and save" in {
        xmlFileIO.save(gameModel ,gameModel.getMrX(gameModel.players)) should be(true)
        xmlFileIO.load().round should be(1)
      }
    }
  }
}
