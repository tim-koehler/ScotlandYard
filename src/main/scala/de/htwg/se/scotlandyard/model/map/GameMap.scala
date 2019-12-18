package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.core.{GameMaster, MapRenderer}
import de.htwg.se.scotlandyard.model.player.Player

import scala.collection.mutable
import scala.collection.mutable.Map

object GameMap {
  var map: Option[List[String]] = Some(List())
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

  private def updateMapString(): Option[List[String]] ={

    for(s <- GameMaster.stations){
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 - 1, ' ')))
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1, ' ')))
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 + 1, ' ')))
    }

    for(p <- GameMaster.players) {
      if(!p.name.equals("MrX") || GameMaster.checkMrXVisibility()) {
        map = Some(map.get.updated(p.getPosition().tuiCoords._2 - 1, map.get(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1 - 1, p.name(0))))
        map = Some(map.get.updated(p.getPosition().tuiCoords._2 - 1, map.get(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1, p.name(1))))
        map = Some(map.get.updated(p.getPosition().tuiCoords._2 - 1, map.get(p.getPosition().tuiCoords._2 - 1).updated(p.getPosition().tuiCoords._1 + 1, p.name(2))))
      }
    }
    map
  }

  override def toString: String = {
    MapRenderer.renderMap()
  }
}
