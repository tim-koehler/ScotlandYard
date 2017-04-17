package de.htwg.se.yourgame.model

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerSpec extends FlatSpec with Matchers {
  "A Player" should "have a name" in {
    Player("Your Name").name should be("Your Name")
  }
}
