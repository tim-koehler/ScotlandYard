package de.htwg.se.scotlandyard.model.fileIOComponent

import de.htwg.se.scotlandyard.model.fileIOComponent.fileIOJsonImpl.FileIO
import de.htwg.se.scotlandyard.model.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class FileIOSpec extends WordSpec with Matchers with PrivateMethodTester {
  "FileIO" when {
    "new" should {
      val gameInitializer = new GameInitializer();
      gameInitializer.initialize(3)

      val jsonFileIO = new FileIO(gameInitializer)

      val xmlFileIO = new fileIOXmlImpl.FileIO(gameInitializer)

      jsonFileIO.pathname = "ScotlandYard_test.json"
      xmlFileIO.pathname = "ScotlandYard_test.xml"

      "json load and save" in {
        jsonFileIO.save() should be(true)
        jsonFileIO.load() should be(true)
      }
      "xml load and save" in {
        xmlFileIO.save() should be(true)
        xmlFileIO.load() should be(true)
      }
    }
  }
}
