package de.htwg.se.scotlandyard.model.coreComponent

import com.google.inject.{Guice, Inject}
import de.htwg.se.scotlandyard.{ScotlandYardModule, model}
import de.htwg.se.scotlandyard.model.{Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.coreComponent.gameInitializerComponent.GameInitializerInterface
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import TicketType.TicketType

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
  var stuckPlayers = scala.collection.mutable.Set[DetectiveInterface]()

  val WINNING_ROUND = 24 //24

  def initialize(nPlayers: Int = 3): Boolean = {
    round = 1
    totalRound = 1
    win = false

    if(!gameInitializer.initialize(nPlayers)) {
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
    players(0).asInstanceOf[MrXInterface]
  }

  def getCurrentPlayerIndex(): Integer = {
    if(round % players.length == 0) {
      (players.length - 1)
    } else {
      ((round % players.length) - 1)
    }
  }

  def nextRound(): Integer = {
    round += 1
    updateTotalRound()
    updateMrXVisibility()
    checkMrXWin()
    if(!checkIfPlayerIsAbleToMove()){
      stuckPlayers.add(getCurrentPlayer())
      if(stuckPlayers.size == players.size - 1) {
        gameRunning = false
        win = true
        winningPlayer = players(0)
      } else {
        nextRound()
      }
    }
    round
  }

  def checkMrXWin(): Boolean = {
    if(round == WINNING_ROUND * players.length) {
      gameRunning = false
      win = true
      winningPlayer = players(0)
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
    val playerMrX = players(0)
    playerMrX.asInstanceOf[MrXInterface].isVisible = checkMrXVisibility()
    if(playerMrX.asInstanceOf[MrXInterface].isVisible) {
      playerMrX.asInstanceOf[MrXInterface].lastSeen = players(0).station.number.toString
    }
    playerMrX.asInstanceOf[MrXInterface].isVisible
  }

  def checkMrXVisibility(): Boolean = {
    if(getCurrentPlayerIndex() >= 1) {
      totalRound match {
        case 3 => return true
        case 8 => return true
        case 13 => return true
        case 18 => return true
        case 24 => return true
        case _ => return false
      }
    }
    false
  }

  def checkIfPlayerIsAbleToMove(): Boolean = {
    getCurrentPlayer().station.stationType match {
      case StationType.Taxi =>
        (getCurrentPlayer().tickets.taxiTickets > 0)
      case model.StationType.Bus =>
        (getCurrentPlayer().tickets.taxiTickets > 0 || getCurrentPlayer().tickets.busTickets > 0)
      case model.StationType.Underground =>
        (getCurrentPlayer().tickets.taxiTickets > 0 || getCurrentPlayer().tickets.busTickets > 0 || getCurrentPlayer().tickets.undergroundTickets > 0)
    }
  }

  def validateMove(newPosition: Integer, ticketType: TicketType): Boolean = {

    if (!isTargetStationInBounds(newPosition)) {
      return false
    }

    if (!isMeanOfTransportValid(newPosition, ticketType)) {
      return false
    }

    if (!isTargetStationEmpty(newPosition)) {
      if (players(0).station.number == newPosition) {
        winningPlayer = getCurrentPlayer()
        gameRunning = false
        win = true
        return true
      } else {
        return false
      }
    }
    true
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean ={
    if(newPosition >= GameMaster.stations.size)
      return false
    true
  }

  private def isMeanOfTransportValid(newPosition: Integer, ticketType: TicketType): Boolean = {
    if(ticketType.equals(TicketType.Taxi)) {
      if(!isTaxiMoveValid(newPosition)) {
        return false
      }
    } else if(ticketType.equals(TicketType.Bus)) {
      if(GameMaster.getCurrentPlayer().station.stationType == StationType.Taxi) {
        return false
      }
      if(!isBusMoveValid(newPosition))
        return false
    } else if(ticketType.equals(TicketType.Underground)) {
      if(GameMaster.getCurrentPlayer().station.stationType != StationType.Underground) {
        return false
      }
      if(!isUndergroundMoveValid(newPosition)) {
        return false
      }
    } else {
      if(!GameMaster.getCurrentPlayer().equals(GameMaster.players(0))) {
        return false
      }
      if(!isBlackMoveValid(newPosition)) {
        return false
      }
    }
    true
  }

  private def isTaxiMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().tickets.taxiTickets <= 0)
      return false
    if(!getCurrentPlayer().station.getNeighbourTaxis.contains(stations(newPosition)))
      return false
    true
  }

  private def isBusMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().tickets.busTickets <= 0) return false
    if(!getCurrentPlayer().station.getNeighbourBuses.contains(stations(newPosition)))
      return false
    true
  }

  private def isUndergroundMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().tickets.undergroundTickets <= 0) return false
    if(!getCurrentPlayer().station.getNeighbourUndergrounds.contains(stations(newPosition)))
      return false
    true
  }

  private def isBlackMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets <= 0) return false
    if(getCurrentPlayer().station.getNeighbourTaxis.contains(stations(newPosition))) {
      return true
    } else if(getCurrentPlayer().station.getNeighbourBuses.contains(stations(newPosition))) {
      return true
    } else if (getCurrentPlayer().station.getNeighbourUndergrounds.contains(stations(newPosition))) {
      return true
    }
    false
  }

  private def isTargetStationEmpty(newPosition: Integer): Boolean = {
    for(p <- GameMaster.players)
      if(p.station.number == newPosition)
        return false
    true
  }

  def updatePlayerPosition(newPosition: Int): Station = {
    getCurrentPlayer().station = stations(newPosition)
    getCurrentPlayer().station
  }

  def decreaseTickets(ticketType: TicketType): Integer = {
    if(ticketType.equals(TicketType.Taxi)) {
      getCurrentPlayer().tickets.taxiTickets -= 1
      getCurrentPlayer().tickets.taxiTickets
    } else if(ticketType.equals(TicketType.Bus)){
      getCurrentPlayer().tickets.busTickets -= 1
      getCurrentPlayer().tickets.busTickets
    } else if(ticketType.equals(TicketType.Underground)) {
      getCurrentPlayer().tickets.undergroundTickets -= 1
      getCurrentPlayer().tickets.undergroundTickets
    } else {
      getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets -= 1
      getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }

  def increaseTickets(ticketType: TicketType): Integer = {
    if(ticketType.equals(TicketType.Taxi)) {
      getCurrentPlayer().tickets.taxiTickets += 1
      getCurrentPlayer().tickets.taxiTickets
    } else if(ticketType.equals(TicketType.Bus)){
      getCurrentPlayer().tickets.busTickets += 1
      getCurrentPlayer().tickets.busTickets
    } else if(ticketType.equals(TicketType.Underground)){
      getCurrentPlayer().tickets.undergroundTickets += 1
      getCurrentPlayer().tickets.undergroundTickets
    } else {
      getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets += 1
      getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets
    }
  }
}
