package de.htwg.se.scotlandyard.model

case class Coordinate(
                     x: Int = 1,
                     y: Int = 1
                     )
{
  def distance (coordinate: Coordinate): Double = {
    val px = coordinate.x - this.x
    val py = coordinate.y - this.y
    Math.sqrt (px * px + py * py)
  }
}
