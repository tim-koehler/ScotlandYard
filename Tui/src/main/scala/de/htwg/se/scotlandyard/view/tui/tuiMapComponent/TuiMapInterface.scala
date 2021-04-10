package de.htwg.se.scotlandyard.view.tui.tuiMapComponent

import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.players.Player

import scala.collection.mutable

trait TuiMapInterface {

  var map: Option[List[String]]
  var playerPositions: mutable.Map[Player, Int]

  var viewOffsetX: Int
  var viewOffsetY: Int

  val mapDisplayDimensionsX: Int
  val mapDisplayDimensionsY: Int
  val mapBorderOffset: Int

  val mapMoveOffset: Int

  def updatePlayerPositions(controller: ControllerInterface): Unit
  def updateViewOffsetX(moveMultiplicator: Int, positive: Boolean ): Int
  def updateViewOffsetY(moveMultiplicator: Int, positive: Boolean ): Int
}
