package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import de.htwg.se.scotlandyard.model.player._

import scala.io.StdIn.readLine

object GameMaster {
  val defaultStation = new Station(0, StationType.Taxi, null, null, null)
  var Player1 = new MrX(defaultStation)
  var Player2 = new Detective(defaultStation)
  var players: List[Player] = List(Player1, Player2)

  def addDefaultPlayers(n: Int): Unit = {
    players =  List(Player1)
    for(i <- 1 to (n - 1)) {
      players = new Detective(defaultStation, "Dt" + i.toString) :: players
    }
  }

  def startGame(): Boolean = {

    if(!GameInitializer.initialize()) {
      false
    }
    true
  }

}
