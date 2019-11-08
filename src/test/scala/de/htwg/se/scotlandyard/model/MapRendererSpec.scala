package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.core.MapRenderer
import org.scalatest._

class MapRendererSpec extends WordSpec with Matchers {
  "MapRenderer.updateX" should {
    "return a positive offset" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.updateX(3, true) shouldBe >(0)
    }
  }
  "MapRenderer.updateY" should {
    "return a positive offset" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.updateX(1, false) shouldBe >(0)
    }
  }
  "MapRenderer.render" should {
    "return a non empty String" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.renderMap() should not be("")
    }
  }
  "MapRenderer readFromFile" should {
    "return a non empty String" in {
      MapRenderer.readMapFromFile() should not be("")
    }
  }
  "MapRenderer init()" should {
    "return true in MapRenderer.init()" in {
      MapRenderer.init() should be(true)
    }
  }
}
