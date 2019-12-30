package de.htwg.se.scotlandyard.model.playersComponent

import org.scalatest._

class DetectiveSpec extends WordSpec with Matchers {
  "A Player" when {
    "new" should {
      //val player = Player("Your Name")
      "have a name" in {
        //player.name should be("Your Name")
      }
      "have a nice String representation" in {
        //player.toString should be("Your Name")
      }
    }
  }
}
