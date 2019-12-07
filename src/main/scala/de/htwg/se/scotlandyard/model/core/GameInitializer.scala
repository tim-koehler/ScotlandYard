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
    StationFactory.resetCounter()
    GameMaster.stations = initStations()

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

  def drawDetectivePosition(isDebugMode: Boolean = ScotlandYard.isDebugMode): Int = {
    if(isDebugMode) {
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

  def drawMisterXPosition(isDebugMode: Boolean = ScotlandYard.isDebugMode): Int = {
    if(isDebugMode) {
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

  def initStations(): List[Station] = {

    val stations = createStations()
    setNeighbours(stations)

    stations
  }

  def createStations(): List[Station] = {
    var stationsBuffer = new ListBuffer[Station]()

    stationsBuffer += StationFactory.createZeroIndexStation()

    var path = ""
    if(ScotlandYard.isDebugMode)
      path = "./src/main/scala/de/htwg/se/scotlandyard/stations_debug.dat"
    else
      path = "./src/main/scala/de/htwg/se/scotlandyard/stations.dat"

    Try(Source.fromFile(path)) match {
      case Success(v) =>
        for (line <- v.getLines()) {
          stationsBuffer += parseCreateStationLine(line)
        }
        v.close()
      case Failure(e) => None
    }

    stationsBuffer.toList
  }

  private def parseCreateStationLine(line: String): Station = {
    val args = splitLine(line)

    if(args(1).equalsIgnoreCase("t"))
      StationFactory.createTaxiStation(args(3).toInt, args(2).toInt)
    else if(args(1).equalsIgnoreCase("b"))
      StationFactory.createBusStation(args(3).toInt, args(2).toInt)
    else
      StationFactory.createUndergroundStation(args(3).toInt, args(2).toInt)
  }

  private def setNeighbours(stations: List[Station]): Integer = {

    var path = ""
    if(ScotlandYard.isDebugMode)
      path = "./src/main/scala/de/htwg/se/scotlandyard/neighbours_debug.dat"
    else
      path = "./src/main/scala/de/htwg/se/scotlandyard/neighbours.dat"

    Try(Source.fromFile(path)) match {
      case Success(v) =>
        for (line <- v.getLines()) {
          parseNeighbourStationLine(stations, line)
        }
        v.close()
      case Failure(e) => None
    }

    stations.size
  }

  private def parseNeighbourStationLine(stations: List[Station], line: String): Set[Station] = {
    val args = splitLine(line)
    var buffer = new ListBuffer[Station]()

    for(i <- 2 to (args.length - 1))
      buffer += stations(args(i).toInt)

    if(args(1).toLowerCase().equalsIgnoreCase("t")) {
      stations(args(0).toInt).setNeighbourTaxis(buffer.toSet)
      stations(args(0).toInt).neighbourUndergrounds
    } else if (args(1).toLowerCase().equalsIgnoreCase("b")) {
      stations(args(0).toInt).setNeighbourBuses(buffer.toSet)
      stations(args(0).toInt).neighbourUndergrounds
    } else {
      stations(args(0).toInt).setNeighbourUndergrounds(buffer.toSet)
      stations(args(0).toInt).neighbourUndergrounds
    }
  }

  private def splitLine(line: String) = line.split("\\s").filter(_.nonEmpty)
}
