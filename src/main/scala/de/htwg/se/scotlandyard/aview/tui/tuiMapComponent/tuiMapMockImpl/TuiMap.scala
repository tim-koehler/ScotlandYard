package de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.tuiMapMockImpl

import de.htwg.se.scotlandyard.aview.tui.tuiMapComponent.TuiMapInterface
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface

import scala.collection.mutable

class TuiMap extends TuiMapInterface{
  override var map: Option[List[String]] = _
  override var playerPositions: mutable.Map[DetectiveInterface, Int] = _
  override var viewOffsetX: Int = _
  override var viewOffsetY: Int = _
  override val mapDisplayDimensionsX: Int = 300
  override val mapDisplayDimensionsY: Int = 200
  override val mapBorderOffset: Int = 1
  override val mapMoveOffset: Int = 5

  override def updatePlayerPositions(): Unit = {}

  override def updateViewOffsetX(moveMultiplicator: Int, positive: Boolean): Int = {5}

  override def updateViewOffsetY(moveMultiplicator: Int, positive: Boolean): Int = {7}
}
