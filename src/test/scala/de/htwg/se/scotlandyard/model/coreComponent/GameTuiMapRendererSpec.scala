package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.tuiMapComponent.baseTuiMapImpl.TuiMap
import org.scalatest._

class GameTuiMapRendererSpec extends WordSpec with Matchers with PrivateMethodTester {
  "MapRenderer.updateX" should {
    "return a positive offset" in {
      TuiMap.viewOffsetX = 10
      TuiMap.viewOffsetY = 10
      TuiMap.updateViewOffsetX(3, true) shouldBe >(0)
      TuiMap.updateViewOffsetX(3, false) shouldBe >(0)
    }
    "and keepInBoundsX() should return a non negativ number" in {
      TuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
      TuiMap.viewOffsetX = -1
      TuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
      TuiMap.viewOffsetX = 1000
      TuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
    }
  }
  "MapRenderer.updateY" should {
    "return a positive offset" in {
      TuiMap.viewOffsetX = 10
      TuiMap.viewOffsetY = 10
      TuiMap.updateViewOffsetY(1, false) shouldBe >(0)
      TuiMap.updateViewOffsetY(1, true) shouldBe >(0)
    }
    "and keepInBoundsY() should return a non negativ number" in {
      TuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsY"))() should be >= 0
      TuiMap.viewOffsetY = -1
      TuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsY"))() should be >= 0
    }
  }
  "MapRenderer.render" should {
    "return a non empty String" in {
      TuiMap.viewOffsetX = 10
      TuiMap.viewOffsetY = 10
      TuiMap.toString() should not be("")
    }
  }
  "MapRenderer init()" should {
    "return true in MapRenderer.init()" in {
      TuiMap.init() should be(true)
    }
  }
}
