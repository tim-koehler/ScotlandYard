package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.ScotlandYard
import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.{DetectiveInterface, MrXInterface}
import de.htwg.se.scotlandyard.util.TicketType
import de.htwg.se.scotlandyard.util.TicketType.TicketType

object GameMaster {
  var stations: List[Station] = List()
  var players: List[DetectiveInterface] = List()
  var round = 1 // counter of moves (increases by 1 when a player moved)
  var totalRound = 1 // number of total rounds (increases by 1 when every player has moved once)
  var win = false
  var winningPlayer: DetectiveInterface = _
  val winningRound = 24 //24

  def startGame(): Boolean = {
    if(!GameInitializer.initialize()) {
      return false
    }
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
    round
  }

  def checkMrXWin(): Boolean = {
    if(round == winningRound * players.length) {
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
      playerMrX.asInstanceOf[MrXInterface].lastSeen = players(0).getPosition().number.toString
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

  def validateMove(newPosition: Integer, ticketType: TicketType): Boolean = {

    if (!isTargetStationInBounds(newPosition)) {
      return false
    }

    if (!isMeanOfTransportValid(newPosition, ticketType)) {
      return false
    }

    if (!isTargetStationEmpty(newPosition)) {
      if (players(0).getPosition().number == newPosition) {
        winningPlayer = getCurrentPlayer()
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
      if(GameMaster.getCurrentPlayer().getPosition().sType == StationType.Taxi) {
        return false
      }
      if(!isBusMoveValid(newPosition))
        return false
    } else if(ticketType.equals(TicketType.Underground)) {
      if(GameMaster.getCurrentPlayer().getPosition().sType != StationType.Underground) {
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
    if(getCurrentPlayer().taxiTickets <= 0)
      return false
    if(!getCurrentPlayer().getPosition().neighbourTaxis.contains(stations(newPosition)))
      return false
    true
  }

  private def isBusMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().busTickets <= 0) return false
    if(!getCurrentPlayer().getPosition().neighbourBuses.contains(stations(newPosition)))
      return false
    true
  }

  private def isUndergroundMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().undergroundTickets <= 0) return false
    if(!getCurrentPlayer().getPosition().neighbourUndergrounds.contains(stations(newPosition)))
      return false
    true
  }

  private def isBlackMoveValid(newPosition: Int): Boolean = {
    if(getCurrentPlayer().asInstanceOf[MrXInterface].blackTickets <= 0) return false
    if(getCurrentPlayer().getPosition().neighbourTaxis.contains(stations(newPosition))) {
      return true
    } else if(getCurrentPlayer().getPosition().neighbourBuses.contains(stations(newPosition))) {
      return true
    } else if (!getCurrentPlayer().getPosition().neighbourUndergrounds.contains(stations(newPosition))) {
      return true
    }
    false
  }

  private def isTargetStationEmpty(newPosition: Integer): Boolean = {
    for(p <- GameMaster.players)
      if(p.getPosition().number == newPosition)
        return false
    true
  }

  def updatePlayerPosition(newPosition: Int): Station = {
    getCurrentPlayer().station = stations(newPosition)
    getCurrentPlayer().station
  }

  def decreaseTickets(ticketType: TicketType): Integer = {
    if(ticketType.equals(TicketType.Taxi)) {
      getCurrentPlayer().taxiTickets -= 1
      getCurrentPlayer().taxiTickets
    } else if(ticketType.equals(TicketType.Bus)){
      getCurrentPlayer().busTickets -= 1
      getCurrentPlayer().busTickets
    } else if(ticketType.equals(TicketType.Underground)) {
      getCurrentPlayer().undergroundTickets -= 1
      getCurrentPlayer().undergroundTickets
    } else {
      getCurrentPlayer().asInstanceOf[MrXInterface].blackTickets -= 1
      getCurrentPlayer().asInstanceOf[MrXInterface].blackTickets
    }
  }

  def increaseTickets(ticketType: TicketType): Integer = {
    if(ticketType.equals(TicketType.Taxi)) {
      getCurrentPlayer().taxiTickets += 1
      getCurrentPlayer().taxiTickets
    } else if(ticketType.equals(TicketType.Bus)){
      getCurrentPlayer().busTickets += 1
      getCurrentPlayer().busTickets
    } else if(ticketType.equals(TicketType.Underground)){
      getCurrentPlayer().undergroundTickets += 1
      getCurrentPlayer().undergroundTickets
    } else {
      getCurrentPlayer().asInstanceOf[MrXInterface].blackTickets += 1
      getCurrentPlayer().asInstanceOf[MrXInterface].blackTickets
    }
  }
}
