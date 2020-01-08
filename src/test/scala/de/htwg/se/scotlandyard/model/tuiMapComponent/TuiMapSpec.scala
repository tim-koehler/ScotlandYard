package de.htwg.se.scotlandyard.model.tuiMapComponent

import com.google.inject.Injector
import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl.TuiMap
import org.scalatest._

class TuiMapSpec extends WordSpec with Matchers {
  "A GameMap" when {
    "new" should {
      val map = new TuiMap()
      "should have a non empty String" in {
        map.toString should not be("")
      }
    }
  }
}
