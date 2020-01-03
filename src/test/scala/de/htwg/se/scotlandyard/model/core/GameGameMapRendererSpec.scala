package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.GameMapRenderer
import org.scalatest._

class GameGameMapRendererSpec extends WordSpec with Matchers {
  "MapRenderer.updateX" should {
    "return a positive offset" in {
      GameMapRenderer.offsetX = 10
      GameMapRenderer.offsetY = 10
      GameMapRenderer.updateX(3, true) shouldBe >(0)
      GameMapRenderer.updateX(3, false) shouldBe >(0)
    }
    "and keepInBoundsX() should return a non negativ number" in {
      GameMapRenderer.keepInBoundsX() should be >= 0
      GameMapRenderer.offsetX = -1
      GameMapRenderer.keepInBoundsX() should be >= 0
      GameMapRenderer.offsetX = 1000
      GameMapRenderer.keepInBoundsX() should be >= 0
    }
  }
  "MapRenderer.updateY" should {
    "return a positive offset" in {
      GameMapRenderer.offsetX = 10
      GameMapRenderer.offsetY = 10
      GameMapRenderer.updateY(1, false) shouldBe >(0)
      GameMapRenderer.updateY(1, true) shouldBe >(0)
    }
    "and keepInBoundsY() should return a non negativ number" in {
      GameMapRenderer.keepInBoundsY() should be >= 0
      GameMapRenderer.offsetY = -1
      GameMapRenderer.keepInBoundsY() should be >= 0
    }
  }
  "MapRenderer.render" should {
    "return a non empty String" in {
      GameMapRenderer.offsetX = 10
      GameMapRenderer.offsetY = 10
      GameMapRenderer.renderMap() should not be("")
    }
  }
  "MapRenderer readFromFile" should {
    "return a non empty String" in {
      GameMapRenderer.readMapFromFile(GameMapRenderer.mapFilePath) should not be("")
    }
  }
  "MapRenderer init()" should {
    "return true in MapRenderer.init()" in {
      GameMapRenderer.init() should be(true)
    }
  }
}
