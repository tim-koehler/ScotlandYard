package de.htwg.se.scotlandyard.controller.fileIOComponent

import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class FileIOSpec extends WordSpec with Matchers with PrivateMethodTester {
  "FileIO" when {
    "new" should {
      val gameInitializer = new GameInitializer();
      gameInitializer.initialize(3)

      val jsonFileIO = new de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOJsonImpl.FileIO(gameInitializer)
      val xmlFileIO = new de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOXmlImpl.FileIO(gameInitializer)

      jsonFileIO.pathname = "ScotlandYard_test.json"
      xmlFileIO.pathname = "ScotlandYard_test.xml"

      "json load and save" in {
        jsonFileIO.save(GameModel()) should be(true)
        jsonFileIO.load().round should be(1)
      }
      "xml load and save" in {
        xmlFileIO.save(GameModel()) should be(true)
        xmlFileIO.load().round should be(1)
      }
    }
  }
}
