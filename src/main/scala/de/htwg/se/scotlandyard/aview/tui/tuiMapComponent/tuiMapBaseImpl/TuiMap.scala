package de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapBaseImpl

import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.players.Player
import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Failure, Success, Try}
import java.nio.charset.CodingErrorAction
import scala.io.Codec

class TuiMap extends TuiMapInterface {
  var map: Option[List[String]] = readMapFromFile("./resources/tui_map.txt")
  var playerPositions: mutable.Map[Player, Int] = mutable.Map[Player, Int]()

  var viewOffsetX: Int = 0
  var viewOffsetY: Int = 0

  val mapDisplayDimensionsX = 200
  val mapDisplayDimensionsY = 30
  val mapBorderOffset = 1

  val mapMoveOffset = 5

  def updatePlayerPositions(controller: ControllerInterface): Unit ={
    setPlayerPositions(controller)
    updateMapString(controller)
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

  private def setPlayerPositions(controller: ControllerInterface): mutable.Map[Player, Int] = {
    val players: Vector[Player] = Vector(controller.getMrX).asInstanceOf[Vector[Player]] ++ controller.getDetectives
    for (p <- players){
      playerPositions += (p -> p.station)
    }
    playerPositions
  }

  private def updateMapString(controller: ControllerInterface): Option[List[String]] ={

    for(s <- controller.getStations()){
      map = Some(map.get.updated(s.tuiCoordinates.y - 1, map.get(s.tuiCoordinates.y - 1).updated(s.tuiCoordinates.x - 1, ' ')))
      map = Some(map.get.updated(s.tuiCoordinates.y - 1, map.get(s.tuiCoordinates.y - 1).updated(s.tuiCoordinates.x, ' ')))
      map = Some(map.get.updated(s.tuiCoordinates.y - 1, map.get(s.tuiCoordinates.y - 1).updated(s.tuiCoordinates.x + 1, ' ')))
    }

    val players: Vector[Player] = Vector(controller.getMrX).asInstanceOf[Vector[Player]] ++ controller.getDetectives
    for(p <- players) {
      if(!p.name.equals("MrX")) {
        map = Some(map.get.updated(controller.getStationOfPlayer(p).tuiCoordinates.y - 1, map.get(controller.getStationOfPlayer(p).tuiCoordinates.y - 1).updated(controller.getStationOfPlayer(p).tuiCoordinates.x - 1, p.name(0))))
        map = Some(map.get.updated(controller.getStationOfPlayer(p).tuiCoordinates.y - 1, map.get(controller.getStationOfPlayer(p).tuiCoordinates.y - 1).updated(controller.getStationOfPlayer(p).tuiCoordinates.x, p.name(1))))
        map = Some(map.get.updated(controller.getStationOfPlayer(p).tuiCoordinates.y - 1, map.get(controller.getStationOfPlayer(p).tuiCoordinates.y - 1).updated(controller.getStationOfPlayer(p).tuiCoordinates.x + 1, p.name(2))))
      }
    }
    map
  }

  private def readMapFromFile(path: String): Option[List[String]] = {
    val mapBuffer = new ListBuffer[String]
    implicit val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

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
