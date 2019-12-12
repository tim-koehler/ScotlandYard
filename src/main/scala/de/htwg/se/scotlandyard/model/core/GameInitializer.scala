package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.station.{Station, StationFactory}
import de.htwg.se.scotlandyard.model.map.{GameMap, StationType}
import de.htwg.se.scotlandyard.model.player.{Detective, MrX, Player}

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Failure, Success, Try}

object GameInitializer {

  // real starting positions
  //val detectiveStartPositions = List(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 123, 138, 141, 155, 174) // 16
  //val misterXStartPositions = List(35, 45, 51, 71, 78, 104, 106, 127, 132, 146, 166, 170, 172) // 13
  val detectiveStartPositions = List(1, 2, 3, 4, 5, 20, 19) // for gui testing purpose
  val misterXStartPositions = List(8, 19) // for gui testing purpose
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
    GameMaster.players = List[Player](new MrX(st))
    for(i <- 1 to (nPlayer - 1)) {
      st = GameMaster.stations(drawDetectivePosition())
      GameMaster.players = GameMaster.players:::List(new Detective(st, "Dt" + i))
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
