package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard

import scala.io.{Source, StdIn}
import de.htwg.se.scotlandyard.model.map.GameMap

object MapRenderer {

  var offsetX: Int = 0
  var offsetY: Int = 0

  val renderDimensionX = 200;
  val renderDimensionY = 30;
  val mapBorderOffset = 1;

  val mapMoveOffset = 5;

  val mapFilePath = "./src/main/scala/de/htwg/se/scotlandyard/ScotlandYardMap.txt"
  val tinyMapFilePath = "./src/main/scala/de/htwg/se/scotlandyard/ScotlandYardMapTiny.txt"

  def init(): Boolean = {
    if(ScotlandYard.isDebugMode) {
      GameMap.map = readMapFromFile(tinyMapFilePath)
    } else {
      GameMap.map = readMapFromFile(mapFilePath)
    }

    if(GameMap.map == null) {
      return false
    }
    true
  }

  def readMapFromFile(path: String): List[String] = {
    val source = Source.fromFile(path)
    for (line <- source.getLines()) {
      GameMap.map = line + "\n" :: GameMap.map
    }
    source.close()

    GameMap.map.reverse
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
      offsetY += moveMultiplicator * mapMoveOffset;
    }
    else {
      offsetY -= moveMultiplicator * mapMoveOffset;
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

    GameMap.updatePlayerPositions()

    var str = renderTopBorder()
      for (y <- offsetY until ((renderDimensionY - mapBorderOffset) + offsetY))
      {
        if(y != offsetY) {
            str += "\u2551"
        }
        else{
            str += "\n\u2551"
        }
        for(x <- offsetX until ((renderDimensionX - mapBorderOffset) + offsetX))
        {
          try {
            str += GameMap.map(y).charAt(x)
          }
          catch  {
            case e: Exception => str += " "
          }
        }
        str += "\u2551" + "\n"
      }
      str += renderBottomBorder()
      str + "\n"
    }

  def renderTopBorder(): String = {
    val builder = new StringBuilder()
    builder.append("\u2554")

    renderLine(builder)

    builder.append("\u2557")
    builder.toString()
  }

  def renderBottomBorder(): String = {
    val builder = new StringBuilder()
    builder.append("\u255A")

    renderLine(builder)

    builder.append("\u255D")
    builder.toString()
  }

  def renderLine(builder: StringBuilder): StringBuilder = {
    for(_ <- 1 to renderDimensionX - mapBorderOffset) {
      builder.append("\u2550")
    }
    builder
  }

}
