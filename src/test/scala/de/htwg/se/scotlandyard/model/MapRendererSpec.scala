package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.core.MapRenderer
import de.htwg.se.scotlandyard.model.map._
import org.scalatest._

class MapRendererSpec extends WordSpec with Matchers {
  "MapRenderer" when {
    "init Method" should {
      "return true" in {
        MapRenderer.init() should be(true)
      }
    }
  }
}
