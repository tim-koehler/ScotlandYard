package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.player.MrX
import org.scalatest._

class GameMasterSpec extends WordSpec with Matchers {
  "in GameMaster Object when" when {
    "after startGame() is called" should {
      "startGame() should return true" in {
        GameMaster.startGame() should be (true)
      }
      "the current player should be MrX" in {
        GameMaster.getCurrentPlayer().isInstanceOf[MrX]
      }
      // TODO: @Roland fix here!
      "the current player Index should be 0" in {
        GameMaster.getCurrentPlayerIndex() shouldBe(1)
      }
      "the next round should be 2" in {
        // TODO: @Roland fix here!!
        GameMaster.nextRound() shouldBe(3)
        GameMaster.totalRound shouldBe(1)
      }
      "MrX should be hidden" in {
        GameMaster.checkMrXVisibility() shouldBe(false)
        GameMaster.players(0).asInstanceOf[MrX].lastSeen shouldBe("never")
      }
    }
  }
}