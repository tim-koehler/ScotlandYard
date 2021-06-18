package de.htwg.se.scotlandyard.model


import org.scalatest.{Matchers, PrivateMethodTester, WordSpec}


class GameInitializerSpec extends WordSpec with Matchers with PrivateMethodTester{

  "Coordinate" when {
    "distance is called" should {
      "return the distance" in {
        val coordinate1 = Coordinate()
        val coordinate2 = Coordinate(x = 2, y = 2)
        coordinate1.distance(coordinate2) should equal (1.4142135623730951)
      }
    }
  }
}
