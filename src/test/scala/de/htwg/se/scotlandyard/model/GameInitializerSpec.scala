package de.htwg.se.scotlandyard.model.core

import org.scalatest._

class GameInitializerSpec extends WordSpec with Matchers {
  "GameInitializer" should {
    "draw not a negative starting position" in {
<<<<<<< HEAD
      GameInitializer.drawMisterXPosition() shouldBe >= (35)
      GameInitializer.drawMisterXPosition() shouldBe <= (172)

      GameInitializer.drawDetectivePosition() shouldBe >= (13)
      GameInitializer.drawDetectivePosition() shouldBe <= (174)

=======
      GameInitializer.drawMisterXPosition() shouldBe > (0)
      GameInitializer.drawMisterXPosition() shouldBe < (200)
>>>>>>> 4107505846e5699c777d010bbb18f3b3ccf2c0a6
    }
  }

}