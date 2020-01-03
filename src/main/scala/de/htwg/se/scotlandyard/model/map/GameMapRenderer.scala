package de.htwg.se.scotlandyard.model.map

import scala.io.Source
import scala.util.{Failure, Success, Try}

object GameMapRenderer {

  var offsetX: Int = 0
  var offsetY: Int = 0

  val renderDimensionX = 200;
  val renderDimensionY = 30;
  val mapBorderOffset = 1;

  val mapMoveOffset = 5;

  val mapFilePath = "./resources/ScotlandYardMap.txt"

  def init(): Boolean = {
    GameMap.map = readMapFromFile(mapFilePath)

    GameMap.map match {
      case Some(t) => true
      case None => false
    }
  }

  def readMapFromFile(path: String): Option[List[String]] = {
    Try(Source.fromFile(path)) match {
      case Success(v) => for (line <- v.getLines()) {
        GameMap.map = Some(line + "\n" :: GameMap.map.get)
      }; v.close()
      case Failure(e) => None
    }

    Some(GameMap.map.get.reverse)
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

        Try(for(x <- offsetX until ((renderDimensionX - mapBorderOffset) + offsetX)) str += GameMap.map.get(y).charAt(x)) match {
          case Success(v) => ;
          case Failure(e) => str += " "
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
