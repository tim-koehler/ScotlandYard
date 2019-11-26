package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers {
  "GameInitializer" should {
    "not draw a starting position out of bounds" in {
      if(!ScotlandYard.isDebugMode) {
        GameInitializer.drawMisterXPosition() shouldBe >= (35)
        GameInitializer.drawMisterXPosition() shouldBe <= (172)

        GameInitializer.drawDetectivePosition() shouldBe >= (13)
        GameInitializer.drawDetectivePosition() shouldBe <= (174)
      } else {
        GameInitializer.drawMisterXPosition() shouldBe >= (1)
        GameInitializer.drawMisterXPosition() shouldBe <= (3)

        GameInitializer.drawDetectivePosition() shouldBe >= (1)
        GameInitializer.drawDetectivePosition() shouldBe <= (3)
      }
    }
  }
}