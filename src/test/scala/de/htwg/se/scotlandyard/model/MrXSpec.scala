package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.model.map._
import org.scalatest._

class MrXSpec extends WordSpec with Matchers {
  "MrX" when {
    "new" should {
      //val MrX = new MrX(new Station(12, StationType.Taxi))
      "have a station number" in {
        //MrX.GetPosition().number should be (12)
      }
      "have a station Type" in {
        //MrX.GetPosition().sType should be (StationType.Taxi)
      }
    }
  }
}
