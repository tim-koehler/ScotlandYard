package de.htwg.se.scotlandyard.fileio

import de.htwg.se.scotlandyard.fileio.fileIOXmlImpl.FileIO
import de.htwg.se.scotlandyard.gameinitializer.gameInitializerMockImpl.GameInitializer
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import scala.io.Source
import scala.swing.Dialog
import scala.util.{Failure, Success, Try}

class fileIoXmlSpec extends WordSpec with Matchers with PrivateMethodTester {
  "FileIOXml" when {
    val gameInitializer = new GameInitializer()
    val fileIOJson = new FileIO(gameInitializer)
    val players = Vector(MrX(), Detective(name = "Dt1"), Detective(name = "Dt2"))
    val gameModel = GameModel(players = players)

    "load" should {
      "return a gameModel" in {
        var source = ""
        Try {
          source = Source.fromFile("./resources/stations.json").getLines.mkString
        } match {
          case Success(v) =>
            fileIOJson.load(source).players(1).station.number should be(-1)
          case Failure(e) =>
            fileIOJson.load(Source.fromFile("../resources/stations.json").getLines.mkString).players(1).station.number should be(-1)
        }
      }
    }

    "save" should {
      "return true" in {
        fileIOJson.save(gameModel, gameModel.getMrX(gameModel.players)) should be(true)
      }
    }
  }
}