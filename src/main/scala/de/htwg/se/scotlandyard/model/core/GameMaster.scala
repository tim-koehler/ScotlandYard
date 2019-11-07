package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import de.htwg.se.scotlandyard.model.player._

import scala.io.StdIn.readLine

object GameMaster {
  var numberOfPlayers = 2
  var players: List[Player] = List()
  var playerNames: List[String] = List("MrX", "Detective1", "Detective2", "Detective3")

  def startGame(): Boolean = {


    if(!GameInitializer.initialize()) {
      false
    }
    true
  }

}
