package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map.{Station, StationType}

object GameInitializer {

    val allPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112)

  def Initialize(): Boolean = {
    true
  }

  def drawDetectivePosition(): Int = {
    5
  }

  def drawMisterXPosition(): Int = {
    val r = scala.util.Random
    r.nextInt(allPositions.length)
  }

  def LoadMapFromFile(): String = {
    ""
  }

  def InitStations(): List[Station] = {
    List(Station(1, StationType.Underground))
  }

}
