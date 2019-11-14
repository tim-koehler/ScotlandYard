package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.map._
import org.scalatest._

class GameMapSpec extends WordSpec with Matchers {
  "A GameMap" when {
    "new" should {
      val map = GameMap
      "have an empty PLayerPositions List" in {
        //map.playerPositions.length should be(0)
      }
      "have an empty stations List" in {
        //map.stations.length should be(0)
      }
    }
  }
}
