package de.htwg.se.scotlandyard.model.fileIoComponent

import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.fileIoComponent.fileIOJsonImpl.FileIO
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class FileIOSpec extends WordSpec with Matchers with PrivateMethodTester {
  "Controller" when {
    "new" should {
      val jsonFileIO = new FileIO()
      jsonFileIO.gameInitializer = new GameInitializer()

      val xmlFileIO = new fileIOXmlImpl.FileIO()
      xmlFileIO.gameInitializer = new GameInitializer()

      jsonFileIO.pathname = "ScotlandYard_test.json"
      xmlFileIO.pathname = "ScotlandYard_test.xml"

      "json load and save" in {
        jsonFileIO.load() should be(true)
        jsonFileIO.save() should be(true)
      }
      "xml load and save" in {
        xmlFileIO.load() should be(true)
        xmlFileIO.save() should be(true)
      }
    }
  }
}
