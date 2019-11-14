package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.{Station, StationType}
import de.htwg.se.scotlandyard.model.player.{Detective, MrX, Player}

object GameInitializer {

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
    initPlayers()
    distributeTicketsToMrX()
    distributeTicketsToDetectives()
    initStations()
    true
  }

  def initPlayers(): Boolean = {
    var st = new Station(drawMisterXPosition(), StationType.Bus, null, null, null)
  GameMaster.players(0).station = st
    for(i <- 1 to (GameMaster.players.length - 1)) {
      st = new Station(drawDetectivePosition(), StationType.Taxi, null, null, null)
      GameMaster.players(i).station = st
    }
    drawnPositions = List()
    true
  }

  def drawDetectivePosition(): Int = {
    var startPosIndex = 0
    do {
      startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    }
    while(drawnPositions.contains(startPosIndex))
    drawnPositions = startPosIndex :: drawnPositions
    detectiveStartPositions(startPosIndex)
  }

  def drawMisterXPosition(): Int = {
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

  def initStations(): List[Station] = {
    if(ScotlandYard.isDebugMode) {
      de.htwg.se.scotlandyard.model.map.GameMap.initStationsDebugMode()
    } else {
      de.htwg.se.scotlandyard.model.map.GameMap.initStations()
    }
    null
  }

}
