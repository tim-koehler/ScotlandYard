package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.core.{GameMaster, MapRenderer}
import de.htwg.se.scotlandyard.model.player.Player

import scala.collection.mutable
import scala.collection.mutable.Map

object GameMap {
  var map: List[String] = List()
  var playerPositions: mutable.Map[Player, Int] = mutable.Map[Player, Int]()

  def updatePlayerPositions(): Unit ={
    setPlayerPositions()
    updateMapString()
  }

  private def setPlayerPositions(): mutable.Map[Player, Int] = {
    for (p <- GameMaster.players){
      playerPositions += (p -> p.getPosition().number)
    }
    playerPositions
  }

  private def updateMapString(): List[String] ={

    for(s <- GameMaster.stations){
      map = map.updated(s.tuiCoords._2 - 1, map(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 - 1, ' '))
      map = map.updated(s.tuiCoords._2 - 1, map(s.tuiCoords._2 - 1).updated(s.tuiCoords._1, ' '))
      map = map.updated(s.tuiCoords._2 - 1, map(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 + 1, ' '))
    }

    for(p <- GameMaster.players) {
      if(!p.name.equals("MrX") || GameMaster.checkMrXVisibility()) {
        map = map.updated(p.getPosition().tuiCoords._2 - 1, map(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1 - 1, p.name(0)))
        map = map.updated(p.getPosition().tuiCoords._2 - 1, map(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1, p.name(1)))
        map = map.updated(p.getPosition().tuiCoords._2 - 1, map(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1 + 1, p.name(2)))
      }
    }
    map
  }

  override def toString: String = {
    MapRenderer.renderMap()
  }
}
