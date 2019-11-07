package de.htwg.se.scotlandyard.model

import de.htwg.se.scotlandyard.model.core.MapRenderer
import org.scalatest._

class MapRendererSpec extends WordSpec with Matchers {
  "MapRenderer.keepInBoundsX" should {
    "return a positive offset" in {
      MapRenderer.offsetX = 10
      MapRenderer.offsetY = 10
      MapRenderer.keepInBoundsX() shouldBe > (0)
    }
  }
}
