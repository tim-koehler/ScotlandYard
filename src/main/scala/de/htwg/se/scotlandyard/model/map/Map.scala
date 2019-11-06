package de.htwg.se.scotlandyard.model.map

import de.htwg.se.scotlandyard.model.core.MapRenderer

object Map {
  val stations = Set[Station]()
  var playerPositions = List()

  override def toString: String = {

    MapRenderer.renderMap()

  }
}
