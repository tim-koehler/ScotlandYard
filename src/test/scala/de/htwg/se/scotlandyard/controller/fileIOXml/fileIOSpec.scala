package de.htwg.se.scotlandyard.controller.fileIOXml

import de.htwg.se.scotlandyard.controller.fileIOComponent.fileIOXmlImpl.FileIO
import de.htwg.se.scotlandyard.controller.gameInitializerComponent.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

class fileIOSpec extends WordSpec with Matchers with PrivateMethodTester {
  "FileIOXml" when {

    val gameInitializer = new GameInitializer()
    val fileIOJson = new FileIO(gameInitializer)
    val players = Vector(MrX(), Detective(name = "Dt1"), Detective(name = "Dt2"))
    val gameModel = GameModel(players = players)

    "save" should {
      "return true" in {
        fileIOJson.save(gameModel, gameModel.getMrX(gameModel.players)) should be(true)
      }
      "load" should {
        "return a gameModel" in {
          fileIOJson.load().players(1).station.number should be(-1)
        }
      }
    }
  }
}
