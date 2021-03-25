package de.htwg.se.scotlandyard.aview.tui.tuiMapComponent

import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

import scala.collection.mutable

trait TuiMapInterface {

  var map: Option[List[String]]
  var playerPositions: mutable.Map[DetectiveInterface, Int]

  var viewOffsetX: Int
  var viewOffsetY: Int

  val mapDisplayDimensionsX: Int
  val mapDisplayDimensionsY: Int
  val mapBorderOffset: Int

  val mapMoveOffset: Int

  def updatePlayerPositions(): Unit
  def updateViewOffsetX(moveMultiplicator: Int, positive: Boolean ): Int
  def updateViewOffsetY(moveMultiplicator: Int, positive: Boolean ): Int
}
