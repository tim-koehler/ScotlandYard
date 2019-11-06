package de.htwg.se.scotlandyard.model.core

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
    //initPlayers()
    distributeTicketsToMrX()
    distributeTicketsToDetectives()
    true
  }

  def initPlayers(): Boolean = {
    var st = new Station(drawMisterXPosition(), StationType.Bus, null, null, null)
    var playerList: List[Player] = List(new MrX(st, "MrX"))
    for(i <- 1 to GameMaster.numberOfPlayers - 1) {
      st = new Station(drawDetectivePosition(), StationType.Taxi, null, null, null)
      playerList = new Detective(st, "Detective" + i.toString) :: playerList
    }
    GameMaster.players = playerList.reverse
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
    GameMaster.players(0).taxiTickets = 99
    GameMaster.players(0).busTickets = 99
    GameMaster.players(0).undergroundTickets = 99
    true
  }

  def distributeTicketsToDetectives(): Boolean = {
    for(i <- 1 to GameMaster.numberOfPlayers - 1) {
      GameMaster.players(i).taxiTickets = numberOfTaxiTickets
      GameMaster.players(i).busTickets = numberOfBusTickets
      GameMaster.players(i).undergroundTickets = numberOfUndergroundTickets
    }
    true
  }

  def loadMapFromFile(): String = {
    ""
  }

  def initStations(): List[Station] = {
    //List(Station(1, StationType.Underground))
    null
  }

}
