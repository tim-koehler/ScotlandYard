package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.core.MapRenderer
import org.scalatest._

class GameMapRendererSpec extends WordSpec with Matchers {
  "MapRenderer.updateX" should {
    "return a positive offset" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.updateX(3, true) shouldBe >(0)
      MapRenderer.updateX(3, false) shouldBe >(0)
    }
    "and keepInBoundsX() should return a non negativ number" in {
      MapRenderer.keepInBoundsX() should be >= 0
      MapRenderer.offsetX = -1
      MapRenderer.keepInBoundsX() should be >= 0
      MapRenderer.offsetX = 1000
      MapRenderer.keepInBoundsX() should be >= 0
    }
  }
  "MapRenderer.updateY" should {
    "return a positive offset" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.updateY(1, false) shouldBe >(0)
      MapRenderer.updateY(1, true) shouldBe >(0)
    }
    "and keepInBoundsY() should return a non negativ number" in {
      MapRenderer.keepInBoundsY() should be >= 0
      MapRenderer.offsetY = -1
      MapRenderer.keepInBoundsY() should be >= 0
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
      MapRenderer.readMapFromFile(MapRenderer.mapFilePath) should not be("")
    }
  }
  "MapRenderer init()" should {
    "return true in MapRenderer.init()" in {
      MapRenderer.init() should be(true)
    }
  }
}
