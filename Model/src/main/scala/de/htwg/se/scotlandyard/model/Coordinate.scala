package de.htwg.se.scotlandyard.model

case class Coordinate(
                     x: Int = 1,
                     y: Int = 1
                     )
{
  def distance (px: Int, py: Int): Double = {
    val ppx = px - this.x
    val ppy = py - this.y
    Math.sqrt (ppx * ppx + ppy * ppy)
  }
}
