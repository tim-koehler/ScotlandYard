package de.htwg.se.scotlandyard.model.core

import scala.io.{Source, StdIn}
import de.htwg.se.scotlandyard.model.map.Map

object MapRenderer {

  var offsetX: Int = 0
  var offsetY: Int = 0

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

  def updateX(newX: Integer): Int ={
    offsetX += newX
    if(offsetX < 0) {
      offsetX = 0
    };
    offsetX
  }

  def updateY(newY: Integer): Int ={
    offsetY += newY
    if(offsetY < 0) {
      offsetY = 0
    };
    offsetY
  }

  def renderMap(): String = {
      var str = ""
      for (i <- offsetY until (35 + offsetY); j <- offsetX until (300 + offsetX))
      {
        try {
          str += Map.map(i).charAt(j)
        }
        catch  {
          case e: Exception => str += " "
        }
      }
      str + "\n"
    }
}
