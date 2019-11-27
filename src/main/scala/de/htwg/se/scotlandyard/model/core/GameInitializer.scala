package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.map.{GameMap, StationType}
import de.htwg.se.scotlandyard.model.player.{Detective, MrX, Player}

import scala.collection.mutable.ListBuffer

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
    if(ScotlandYard.isDebugMode) {
      GameMaster.stations = initDebugStations()
    }
    else {
      GameMaster.stations = initStations()
    }
    true
  }

  def initPlayers(nPlayer: Int): Boolean = {
    var st = GameMaster.stations(drawMisterXPosition())
    GameMaster.players = List[Player](new MrX(st))
    for(i <- 1 to (nPlayer - 1)) {
      st = GameMaster.stations(drawDetectivePosition())
      GameMaster.players = GameMaster.players:::List(new Detective(st, "Dt" + i))
    }
    distributeTicketsToMrX()
    distributeTicketsToDetectives()
    GameMap.updatePlayerPositions()
    true
  }

  def drawDetectivePosition(): Int = {
    if(ScotlandYard.isDebugMode) {
      return 2
    }
    var startPosIndex = 0
    do {
      startPosIndex = r.nextInt(MAX_DETECTIVE_LIST_INDEX)
    }
    while(drawnPositions.contains(startPosIndex))
    drawnPositions = startPosIndex :: drawnPositions
    detectiveStartPositions(startPosIndex)
  }

  def drawMisterXPosition(): Int = {
    if(ScotlandYard.isDebugMode) {
      return 1
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

  def initDebugStations(): List[Station] = {

    def createStations(): List[Station] = {
      var stationsBuffer = new ListBuffer[Station]()

      // Zero index Station
      stationsBuffer += new Station(0, StationType.Taxi, null, null, null, (1, 1))
      stationsBuffer += new Station(0, StationType.Taxi, null, null, null, (1, 1))

      stationsBuffer += new Station(1, StationType.Taxi, null, null, null, (5, 20))
      stationsBuffer += new Station(2, StationType.Underground, null, null, null, (23, 25))
      stationsBuffer += new Station(3, StationType.Underground, null, null, null, (20, 7))

      stationsBuffer.toList
    }

    val stations = createStations()

    def setNeighbours(): Int = {
      stations(1).setNeighbourTaxis(Set(stations(2), stations(3)))

      stations(2).setNeighbourTaxis(Set(stations(1)))
      stations(2).setNeighbourBuses(Set(stations(3)))
      stations(2).setNeighbourUndergrounds(Set(stations(3)))

      stations(3).setNeighbourTaxis(Set(stations(1)))
      stations(3).setNeighbourBuses(Set(stations(2)))
      stations(3).setNeighbourUndergrounds(Set(stations(2)))

      stations.size
    }

    setNeighbours()

    stations
  }

  def initStations(): List[Station] = {
    null
  }
}
