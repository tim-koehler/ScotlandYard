package de.htwg.se.scotlandyard.model.tuiMapComponent.tuiMapBaseImpl

import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.tuiMapComponent.TuiMapInterface

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Failure, Success, Try}

class TuiMap extends TuiMapInterface {
  var map: Option[List[String]] = readMapFromFile("./resources/ScotlandYardMap.txt")
  var playerPositions: mutable.Map[DetectiveInterface, Int] = mutable.Map[DetectiveInterface, Int]()

  var viewOffsetX: Int = 0
  var viewOffsetY: Int = 0

  val mapDisplayDimensionsX = 200
  val mapDisplayDimensionsY = 30
  val mapBorderOffset = 1

  val mapMoveOffset = 5

  def updatePlayerPositions(): Unit ={
    setPlayerPositions()
    updateMapString()
  }

  def updateViewOffsetX(moveMultiplicator: Int, positive: Boolean ): Int = {
    if(positive) {
      viewOffsetX += moveMultiplicator * mapMoveOffset;
    }
    else{
      viewOffsetX -= moveMultiplicator * mapMoveOffset;
    }
    keepInBoundsX()
  }

  def updateViewOffsetY(moveMultiplicator: Int, positive: Boolean ): Int = {
    if(positive) {
      viewOffsetY += moveMultiplicator * mapMoveOffset;
    }
    else {
      viewOffsetY -= moveMultiplicator * mapMoveOffset;
    }
    keepInBoundsY()
  }

  private def keepInBoundsX(): Int = {
    if(viewOffsetX < 0) {
      viewOffsetX = 0
    }
    else if(viewOffsetX + mapDisplayDimensionsX > 280) {
      viewOffsetX = 280 - mapDisplayDimensionsX;
    }
    viewOffsetX;
  }

  private def keepInBoundsY(): Int = {
    if(viewOffsetY < 0) {
      viewOffsetY = 0
    }
    viewOffsetY
  }

  private def setPlayerPositions(): mutable.Map[DetectiveInterface, Int] = {
    for (p <- GameMaster.players){
      playerPositions += (p -> p.station.number)
    }
    playerPositions
  }

  private def updateMapString(): Option[List[String]] ={

    for(s <- GameMaster.stations){
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 - 1, ' ')))
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1, ' ')))
      map = Some(map.get.updated(s.tuiCoords._2 - 1, map.get(s.tuiCoords._2 - 1).updated(s.tuiCoords._1 + 1, ' ')))
    }

    for(p <- GameMaster.players) {
      if(!p.name.equals("MrX") || GameMaster.checkMrXVisibility()) {
        map = Some(map.get.updated(p.station.tuiCoords._2 - 1, map.get(p.station.tuiCoords._2 - 1).updated(p.station.tuiCoords._1 - 1, p.name(0))))
        map = Some(map.get.updated(p.station.tuiCoords._2 - 1, map.get(p.station.tuiCoords._2 - 1).updated(p.station.tuiCoords._1, p.name(1))))
        map = Some(map.get.updated(p.station.tuiCoords._2 - 1, map.get(p.station.tuiCoords._2 - 1).updated(p.station.tuiCoords._1 + 1, p.name(2))))
      }
    }
    map
  }

  private def readMapFromFile(path: String): Option[List[String]] = {
    val mapBuffer = new ListBuffer[String]
    Try(Source.fromFile(path)) match {
      case Success(v) =>
        for (line <- v.getLines()) {
          mapBuffer.addOne(line + "\n")
        }
        v.close()
      case Failure(e) => None
    }
    Option(mapBuffer.toList)
  }

  override def toString: String = {

    updatePlayerPositions()

    var str = getTopBorder()
    for (y <- viewOffsetY until ((mapDisplayDimensionsY - mapBorderOffset) + viewOffsetY)) {
      if (y != viewOffsetY) {
        str += "\u2551"
      }
      else {
        str += "\n\u2551"
      }

      Try(for (x <- viewOffsetX until ((mapDisplayDimensionsX - mapBorderOffset) + viewOffsetX)) str += this.map.get(y).charAt(x)) match {
        case Success(v) => ;
        case Failure(e) => str += " "
      }

      str += "\u2551" + "\n"
    }
    str += getBottomBorder()
    str + "\n"
  }

  private def getTopBorder(): String = {
    val builder = new StringBuilder()
    builder.append("\u2554")

    getLine(builder)

    builder.append("\u2557")
    builder.toString()
  }

  private def getBottomBorder(): String = {
    val builder = new StringBuilder()
    builder.append("\u255A")

    getLine(builder)

    builder.append("\u255D")
    builder.toString()
  }

  private def getLine(builder: StringBuilder): StringBuilder = {
    for(_ <- 1 to mapDisplayDimensionsX - mapBorderOffset) {
      builder.append("\u2550")
    }
    builder
  }
}