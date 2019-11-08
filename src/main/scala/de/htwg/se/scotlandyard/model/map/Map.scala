package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.core.MapRenderer
import de.htwg.se.scotlandyard.model.player.Player

object Map {
  var map: List[String] = List()
  val stations: List[Station] = List()
  var playerPositions: Map[Player, Int] = scala.collection.immutable.Map()

  //TODO: CLEANUP!!
  def initStations(): Int = {

    stations = new Station(1) :: stations
    stations(1)
    1
  }

  override def toString: String = {
    MapRenderer.renderMap()
  }
}
