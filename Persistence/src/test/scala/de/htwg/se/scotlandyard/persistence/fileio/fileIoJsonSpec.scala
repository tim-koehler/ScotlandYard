package de.htwg.se.scotlandyard.persistence.fileio

import de.htwg.se.scotlandyard.persistence.fileio.fileIOJsonImpl.FileIO
import de.htwg.se.scotlandyard.model.GameModel
import de.htwg.se.scotlandyard.model.players.{Detective, MrX}
import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}

import scala.io.Source
import scala.util.{Failure, Success, Try}

class fileIoJsonSpec extends WordSpec with Matchers with PrivateMethodTester {

  "FileIOJson" when {

    val fileIOJson = new FileIO()
    val players = Vector(MrX(), Detective(name = "Dt1"), Detective(name = "Dt2"))
    val gameModel = GameModel(players = players)

    "save" should {
      "return true" in {
        fileIOJson.save(gameModel) should be(true)
      }
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
    }
  }
}