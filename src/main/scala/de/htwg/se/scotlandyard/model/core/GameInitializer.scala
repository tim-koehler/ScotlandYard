package de.htwg.se.scotlandyard.model.core

object GameInitializer {

    val allPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112)

  def Initialize(): Boolean = {
    true
  }

  def drawDetectivePosition(): Int = {
    5
  }

  def drawMisterXPosition(): Int = {
    val MAX_STATION_NUMBER = 200
    val r = scala.util.Random
    r.nextInt(MAX_STATION_NUMBER) + 1
  }

}
