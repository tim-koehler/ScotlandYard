package de.htwg.se.scotlandyard.model.core

object GameInitializer {

  // echte Anfangspositionen
  val detectiveStartPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174) // 16
  val misterXStartPositions = List(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172) // 13
  val MAX_DETECTIVE_LIST_INDEX = detectiveStartPositions.length
  val MAX_MISTERX_LIST_INDEX = misterXStartPositions.length
  val r = scala.util.Random

  def initialize(): Boolean = {
    true
  }

  def drawDetectivePosition(): Int = {
    val startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    detectiveStartPositions(startPosIndex)
  }

  def drawMisterXPosition(): Int = {
    val startPosIndex = r.nextInt(MAX_MISTERX_LIST_INDEX)
    misterXStartPositions(startPosIndex)
  }

}
