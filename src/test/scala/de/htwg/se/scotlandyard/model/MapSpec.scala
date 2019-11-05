package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.map._
import org.scalatest._

class MapSpec extends WordSpec with Matchers {
  "A Map" when {
    "new" should {
      val map = Map
      "have an empty PLayerPositions List" in {
        //map.playerPositions.length should be(0)
      }
      "have an empty stations List" in {
        //map.stations.length should be(0)
      }
    }
  }
}
