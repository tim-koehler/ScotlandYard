package de.htwg.se.scotlandyard.model.map

import org.scalatest._

class GameMapSpec extends WordSpec with Matchers {
  "A GameMap" when {
    "new" should {
      val map = GameMap
      "should have a non empty String" in {
        map.toString should not be("")
      }
    }
  }
}
