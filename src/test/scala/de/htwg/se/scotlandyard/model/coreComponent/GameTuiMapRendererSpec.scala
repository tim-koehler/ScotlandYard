package de.htwg.se.scotlandyard.model.coreComponent

import de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl.TuiMap
import org.scalatest._

class GametuiMapRendererSpec extends WordSpec with Matchers with PrivateMethodTester {
  "MapRenderer.updateX" when {
    "new" should {
      val tuiMap = new TuiMap()

      "return a positive offset" in {
        tuiMap.viewOffsetX = 10
        tuiMap.viewOffsetY = 10
        tuiMap.updateViewOffsetX(3, true) shouldBe >(0)
        tuiMap.updateViewOffsetX(3, false) shouldBe >(0)
      }
      "and keepInBoundsX() should return a non negativ number" in {
        tuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
        tuiMap.viewOffsetX = -1
        tuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
        tuiMap.viewOffsetX = 1000
        tuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsX"))() should be >= 0
      }
      "MapRenderer.updateY should return a positive offset" in {
        tuiMap.viewOffsetX = 10
        tuiMap.viewOffsetY = 10
        tuiMap.updateViewOffsetY(1, false) shouldBe >(0)
        tuiMap.updateViewOffsetY(1, true) shouldBe >(0)
      }
      "and keepInBoundsY() should return a non negativ number" in {
        tuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsY"))() should be >= 0
        tuiMap.viewOffsetY = -1
        tuiMap invokePrivate PrivateMethod[Int](Symbol("keepInBoundsY"))() should be >= 0
      }
      "MapRenderer.render return a non empty String" in {
        tuiMap.viewOffsetX = 10
        tuiMap.viewOffsetY = 10
        tuiMap.toString() should not be ("")
      }
    }
  }
}
