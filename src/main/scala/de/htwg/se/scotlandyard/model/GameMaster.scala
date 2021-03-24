package de.htwg.se.scotlandyard.model

import com.google.inject.Guice
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.{ScotlandYardModule, model}

import scala.collection.mutable

object GameMaster {

  var gameInitializer: GameInitializerInterface = Guice.createInjector(new ScotlandYardModule).getInstance(classOf[GameInitializerInterface])

  var stations: List[Station] = List()
  var players: List[DetectiveInterface] = List()
  var round = 1 // counter of moves (increases by 1 when a player moved)
  var totalRound = 1 // number of total rounds (increases by 1 when every player has moved once)
  var win = false
  var gameRunning = false
  var winningPlayer: DetectiveInterface = _
  var stuckPlayers: mutable.Set[DetectiveInterface] = scala.collection.mutable.Set[DetectiveInterface]()

  val WINNING_ROUND = 24 //24

  def initialize(nPlayers: Int = 3): Boolean = {
    round = 1
    totalRound = 1
    win = false

    if (!gameInitializer.initialize(nPlayers)) {
      return false
    }

    players.head.asInstanceOf[MrXInterface].history = mutable.Stack()
    stuckPlayers = scala.collection.mutable.Set[DetectiveInterface]()
    gameRunning = true

    true
  }

  def getCurrentPlayer(): DetectiveInterface = {
    players(getCurrentPlayerIndex())
  }

  def getMrX(): MrXInterface = {
    players.head.asInstanceOf[MrXInterface]
  }

  def getCurrentPlayerIndex(): Integer = {
    if (round % players.length == 0) {
      players.length - 1
    } else {
      (round % players.length) - 1
    }
  }

  def nextRound(): Integer = {
    round += 1
    updateTotalRound()
    updateMrXVisibility()
    checkMrXWin()
    if (!checkIfPlayerIsAbleToMove()) {
      stuckPlayers.add(getCurrentPlayer())
      if (stuckPlayers.size == players.size - 1) {
        endGame(players.head)
      } else {
        nextRound()
      }
    }
    round
  }

  private def checkMrXWin(): Boolean = {
    if (round == WINNING_ROUND * players.length) {
      endGame(players.head)
      true
    } else {
      false
    }
  }

  def previousRound(): Integer = {
    round -= 1
    updateTotalRound()
    updateMrXVisibility()
    round
  }

  def updateTotalRound(): Integer = {
    totalRound = (round.toDouble / players.length.toDouble).ceil.toInt
    totalRound
  }

  def updateMrXVisibility(): Boolean = {
    val playerMrX = players.head
    playerMrX.asInstanceOf[MrXInterface].isVisible = checkMrXVisibility()
    if (playerMrX.asInstanceOf[MrXInterface].isVisible) {
      playerMrX.asInstanceOf[MrXInterface].lastSeen = players.head.station.number.toString
    }
    playerMrX.asInstanceOf[MrXInterface].isVisible
  }

  def checkMrXVisibility(): Boolean = {
      totalRound match {
        case 3 => true
        case 8 => true
        case 13 => true
        case 18 => true
        case 24 => true
        case _ => false
      }
  }

  def validateMove(newPosition: Integer, ticketType: TicketType): Boolean = {
    if (!isTargetStationInBounds(newPosition)) return false
    if (getCurrentPlayer().station.number == newPosition) return false
    if (!isMeanOfTransportValid(newPosition, ticketType)) return false
    if (!isTargetStationEmpty(newPosition)) return false

    if (players.head.station.number == newPosition) {
      endGame(getCurrentPlayer())
    }
    true
  }

  def updatePlayerPosition(newPosition: Int): Station = {
    getCurrentPlayer().station = stations(newPosition)
    getCurrentPlayer().station
  }

  def decreaseTickets(ticketType: TicketType): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer().tickets.taxiTickets -= 1
        getCurrentPlayer().tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer().tickets.busTickets -= 1
        getCurrentPlayer().tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer().tickets.undergroundTickets -= 1
        getCurrentPlayer().tickets.undergroundTickets
      case _ =>
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets -= 1
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }

  def increaseTickets(ticketType: TicketType): Integer = {
    ticketType match {
      case TicketType.Taxi =>
        getCurrentPlayer().tickets.taxiTickets += 1
        getCurrentPlayer().tickets.taxiTickets
      case TicketType.Bus =>
        getCurrentPlayer().tickets.busTickets += 1
        getCurrentPlayer().tickets.busTickets
      case TicketType.Underground =>
        getCurrentPlayer().tickets.undergroundTickets += 1
        getCurrentPlayer().tickets.undergroundTickets
      case _ =>
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets += 1
        getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
  private def checkIfPlayerIsAbleToMove(): Boolean = {
    getCurrentPlayer().station.stationType match {
      case StationType.Taxi =>
        (getCurrentPlayer().tickets.taxiTickets > 0)
      case model.StationType.Bus =>
        (getCurrentPlayer().tickets.taxiTickets > 0 || getCurrentPlayer().tickets.busTickets > 0)
      case model.StationType.Underground =>
        (getCurrentPlayer().tickets.taxiTickets > 0 || getCurrentPlayer().tickets.busTickets > 0 || getCurrentPlayer().tickets.undergroundTickets > 0)
    }
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean = {
    newPosition < GameMaster.stations.size && newPosition > 0
  }

  private def isMeanOfTransportValid(newPosition: Integer, ticketType: TicketType): Boolean = {
    ticketType match {
      case TicketType.Taxi =>
        isTaxiMoveValid(newPosition)
      case TicketType.Bus =>
        if (GameMaster.getCurrentPlayer().station.stationType == StationType.Taxi) return false
        isBusMoveValid(newPosition)
      case TicketType.Underground =>
        if (GameMaster.getCurrentPlayer().station.stationType != StationType.Underground) return false
        isUndergroundMoveValid(newPosition)
      case _ =>
        if (!GameMaster.getCurrentPlayer().equals(GameMaster.players.head)) return false
        isBlackMoveValid(newPosition)
    }
  }

  private def isTaxiMoveValid(newPosition: Int): Boolean = {
    if (getCurrentPlayer().tickets.taxiTickets <= 0) return false
    getCurrentPlayer().station.getNeighbourTaxis.contains(stations(newPosition))
  }

  private def isBusMoveValid(newPosition: Int): Boolean = {
    if (getCurrentPlayer().tickets.busTickets <= 0) return false
    getCurrentPlayer().station.getNeighbourBuses.contains(stations(newPosition))
  }

  private def isUndergroundMoveValid(newPosition: Int): Boolean = {
    if (getCurrentPlayer().tickets.undergroundTickets <= 0) return false
    getCurrentPlayer().station.getNeighbourUndergrounds.contains(stations(newPosition))
  }

  private def isBlackMoveValid(newPosition: Int): Boolean = {
    if (getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets <= 0) return false
    getCurrentPlayer().station.getNeighbourTaxis.contains(stations(newPosition)) ||
      getCurrentPlayer().station.getNeighbourBuses.contains(stations(newPosition)) ||
      getCurrentPlayer().station.getNeighbourUndergrounds.contains(stations(newPosition))
  }

  private def isTargetStationEmpty(newPosition: Integer): Boolean = {
    for (p <- GameMaster.players)
      if (p.station.number == newPosition) return false
    true
  }

  private def endGame(winningPlayer: DetectiveInterface): Boolean = {
    this.winningPlayer = winningPlayer
    this.gameRunning = false
    this.win = true
    true
  }
}
