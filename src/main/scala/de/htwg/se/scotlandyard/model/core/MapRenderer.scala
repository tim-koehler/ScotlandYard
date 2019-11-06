package de.htwg.se.scotlandyard.model.core

import scala.io.{Source, StdIn}

object MapRenderer {

  var map: List[String] = List()
  var offsetX: Int = 0
  var offsetY: Int = 0

  def init() : Boolean = {
    val source = Source.fromFile("./src/main/scala/de/htwg/se/scotlandyard/ScotlandYardMap.txt")
    for (line <- source.getLines()) {
      map = line + "\n" :: map
    }
    map = map.reverse

    source.close()

    if(map == null) {
      return false
    }
    true
  }

  def updateX(newX: Integer): Boolean ={
    offsetX += newX
    true
  }

  def updateY(newY: Integer): Boolean ={
    offsetY += newY
    true
  }

  def renderMap(): String = {
    var str = ""
    for (i <- offsetY until (45 + offsetY); j <- offsetX until (211 + offsetX)) {
        str += map(i).charAt(j)
    }
    println("MapString")
    println(str)
    println("MapString^")
    str
  }
}
