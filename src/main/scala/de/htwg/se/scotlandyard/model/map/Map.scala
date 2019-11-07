package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.core.MapRenderer

object Map {
  var map: List[String] = List()
  val stations: Set[Station] = Set()
  var playerPositions = List()



  override def toString: String = {
    MapRenderer.renderMap()
  }
}
