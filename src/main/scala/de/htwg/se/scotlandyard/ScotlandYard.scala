package de.htwg.se.scotlandyard

import de.htwg.se.scotlandyard.model.core._
import de.htwg.se.scotlandyard.model.player.Player

object ScotlandYard {
  def main(args: Array[String]): Unit = {
    if(GameMaster.StartGame().equals(false)) {
      println("Fehler")
    }
    println("Spiel beendet")
  }
}
