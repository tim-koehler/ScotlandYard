package de.htwg.se.scotlandyard.model.core

import scala.io.{Source, StdIn}
import de.htwg.se.scotlandyard.model.map.Map

object MapRenderer {

  var offsetX: Int = 0
  var offsetY: Int = 0

  val renderDimensionX = 200;
  val renderDimensionY = 33;

  val mapMoveOffset = 5;

  def init() : Boolean = {
    val source = Source.fromFile("./src/main/scala/de/htwg/se/scotlandyard/ScotlandYardMap.txt")
    for (line <- source.getLines()) {
      Map.map = line + "\n" :: Map.map
    }
    source.close()

    Map.map = Map.map.reverse

    if(Map.map == null) {
      return false
    }
    true
  }

  def updateX(moveMultiplicator: Int, positive: Boolean ): Int = {
    if(positive) {
      offsetX += moveMultiplicator * mapMoveOffset;
    }
    else{
      offsetX -= moveMultiplicator * mapMoveOffset;
    }
    keepInBoundsX()
  }

  def keepInBoundsX(): Int = {
    if(offsetX < 0) {
      offsetX = 0
    }
    else if(offsetX + renderDimensionX > 280) {
      offsetX = 280 - renderDimensionX;
    }
    offsetX;
  }

  def updateY(moveMultiplicator: Int, positive: Boolean ): Int = {
    if(positive) {
      offsetX += moveMultiplicator * mapMoveOffset;
    }
    else{
      offsetX -= moveMultiplicator * mapMoveOffset;
    }
    keepInBoundsY()
  }

  def keepInBoundsY(): Int = {
    if(offsetY < 0) {
      offsetY = 0
    }
    offsetY
  }

  def renderMap(): String = {
      var str = ""
      for (y <- offsetY until (renderDimensionY + offsetY))
      {
        for(x <- offsetX until (renderDimensionX + offsetX))
        {
          try {
            str += Map.map(y).charAt(x)
          }
          catch  {
            case e: Exception => str += " "
          }
        }
        str += "\n"
      }
      println("OffsetX: " + offsetX)
      println("OffsetY: " + offsetY)
      str + "\n"
    }
}
