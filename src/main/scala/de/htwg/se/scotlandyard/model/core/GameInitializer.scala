package de.htwg.se.scotlandyard.model.core

import java.awt.Color

import de.htwg.se.scotlandyard.model.map.{GameMap, StationType}
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.{MrX, Detective}

object GameInitializer {

  val MRX_COLOR = Color.BLACK
  val DT1_COLOR = Color.BLUE
  val DT2_COLOR = Color.GREEN
  val DT3_COLOR = Color.ORANGE
  val DT4_COLOR = Color.MAGENTA
  val DT5_COLOR = Color.RED
  val DT6_COLOR = Color.CYAN

  val colorList = List(MRX_COLOR, DT1_COLOR, DT2_COLOR, DT3_COLOR, DT4_COLOR, DT5_COLOR, DT6_COLOR)

  // real starting positions
  val detectiveStartPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174) // 16
  val misterXStartPositions = List(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172) // 13
  val MAX_DETECTIVE_LIST_INDEX = detectiveStartPositions.length
  val MAX_MISTERX_LIST_INDEX = misterXStartPositions.length
  // drawn Detective Positions
  var drawnPositions: List[Int] = List()
  val numberOfTaxiTickets = 11
  val numberOfBusTickets = 8
  val numberOfUndergroundTickets = 4
  val r = scala.util.Random

  def initialize(): Boolean = {
    MapRenderer.init()
    GameMaster.stations = new StationInitializer().initStations()
    initPlayers(3)
    true
  }

  def initPlayers(nPlayer: Int): Boolean = {
    GameMaster.players = List()
    var st = GameMaster.stations(drawMisterXPosition())
    GameMaster.players = List[Detective](new MrX(st))
    for(i <- 1 to (nPlayer - 1)) {
      st = GameMaster.stations(drawDetectivePosition())
      GameMaster.players = GameMaster.players:::List(new Detective(st, "Dt" + i, colorList(i)))
    }
    distributeTicketsToMrX()
    distributeTicketsToDetectives()
    GameMap.updatePlayerPositions()
    drawnPositions = List()
    true
  }

  def drawDetectivePosition(nonRandomPosition: Integer = -1): Int = {
    if(nonRandomPosition != -1) {
      return nonRandomPosition
    }
    var startPosIndex = 0
    do {
      startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    }
    while(drawnPositions.contains(startPosIndex))
    drawnPositions = startPosIndex :: drawnPositions
    detectiveStartPositions(startPosIndex)
  }

  def drawMisterXPosition(nonRandomPosition: Integer = -1): Int = {
    if(nonRandomPosition != -1) {
      return nonRandomPosition
    }
    val startPosIndex = r.nextInt(MAX_MISTERX_LIST_INDEX)
    misterXStartPositions(startPosIndex)
  }

  def distributeTicketsToMrX(): Boolean = {
    distributeTickets(0, 99, 99, 99)
  }

  def distributeTicketsToDetectives(): Boolean = {
    var success = false
    for(i <- 1 to (GameMaster.players.length - 1)) {
      success = distributeTickets(i, numberOfTaxiTickets, numberOfBusTickets, numberOfUndergroundTickets)
    }
    success
  }

  def distributeTickets(index: Int, nTaxi: Int, nBus: Int, nUnder: Int): Boolean = {
    GameMaster.players(index).taxiTickets = nTaxi
    GameMaster.players(index).busTickets = nBus
    GameMaster.players(index).undergroundTickets = nUnder
    true
  }
}
