package de.htwg.se.scotlandyard.gameinitializer

import de.htwg.se.scotlandyard.model.{PersistenceGameModel}

import java.awt.Color

trait GameInitializerInterface {

  val MRX_COLOR: Color
  val DT1_COLOR: Color
  val DT2_COLOR: Color
  val DT3_COLOR: Color
  val DT4_COLOR: Color
  val DT5_COLOR: Color
  val DT6_COLOR: Color

  val detectiveStartPositions: Vector[Int]
  val misterXStartPositions: Vector[Int]
  var MAX_DETECTIVE_LIST_INDEX: Int
  var MAX_MISTERX_LIST_INDEX: Int
  var drawnPositions: Vector[Int]
  val numberOfTaxiTickets: Int
  val numberOfBusTickets: Int
  val numberOfUndergroundTickets: Int
  val r = scala.util.Random

  def initialize(nPlayer: Int): PersistenceGameModel

  def getColorList(): Vector[Color]
}
