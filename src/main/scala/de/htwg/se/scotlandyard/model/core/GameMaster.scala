package de.htwg.se.scotlandyard.model.core

import scala.io.StdIn.readLine

object GameMaster {
  var numberOfPlayer = 2

  def startGame(): Boolean = {


    if(!GameInitializer.initialize()) {
      false
    }
    true
  }
}
