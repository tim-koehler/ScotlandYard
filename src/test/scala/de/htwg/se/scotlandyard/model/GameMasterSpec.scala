package de.htwg.se.scotlandyard.model.core

import org.scalatest._

class GameMasterSpec extends WordSpec with Matchers {
  "GameMaster" should {
    "return true" in {
      GameMaster.startGame() should be (true)
    }
  }

}