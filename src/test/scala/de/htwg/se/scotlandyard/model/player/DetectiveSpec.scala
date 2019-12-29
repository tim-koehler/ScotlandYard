package de.htwg.se.scotlandyard.model.player

import java.awt.Color

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.map.StationType
import de.htwg.se.scotlandyard.model.map.station.{Station, StationFactory}
import org.scalatest._

class DetectiveSpec extends WordSpec with Matchers {
  "Detective" when {
    GameMaster.startGame()
    "new" should {
      val st = StationFactory.createTaxiStation(0, (1, 1))
      val dt1 = new Player(st, "Dt1", Color.MAGENTA)

      "have a station number" in {
        dt1.getPosition().number should be (0)
      }
      "have a station Type" in {
        dt1.getPosition().sType should be (StationType.Taxi)
      }
      "should have a nice String representation" in {
        dt1.toString() shouldBe ("Dt1: 0 (TAXI)  TICKETS-> T: 0, B: 0, U: 0")
      }
    }
  }
}
