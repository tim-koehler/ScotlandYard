package de.htwg.se.scotlandyard.model.tuiMapComponent

import de.htwg.se.scotlandyard.model.tuiMapComponent.baseTuiMapImpl.TuiMap
import org.scalatest._

class TuiMapSpec extends WordSpec with Matchers {
  "A GameMap" when {
    "new" should {
      val map = TuiMap
      "should have a non empty String" in {
        map.toString should not be("")
      }
    }
  }
}
