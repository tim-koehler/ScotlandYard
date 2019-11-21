package de.htwg.se.scotlandyard.model.core

import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers {
  "GameInitializer" should {
    "draw not a negative starting position" in {
      GameInitializer.drawMisterXPosition() shouldBe >= (35)
      GameInitializer.drawMisterXPosition() shouldBe <= (172)

      GameInitializer.drawDetectivePosition() shouldBe >= (13)
      GameInitializer.drawDetectivePosition() shouldBe <= (174)
    }
  }
}