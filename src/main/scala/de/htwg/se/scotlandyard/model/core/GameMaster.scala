package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType

object GameMaster {
  val defaultStation = new Station(0, StationType.Taxi, null, null, null)
  var stations: List[Station] = List()
  var players: List[Player] = List()
  var round = 1
  var totalRound = 1

  def startGame(): Boolean = {
    if(!GameInitializer.initialize()) {
      return false
    }
    true
  }

  def getCurrentPlayer(): Player = {
    players(getCurrentPlayerIndex() - 1)
  }

  def getCurrentPlayerIndex(): Int = {
    if(round % players.length == 0) {
      players.length
    } else {
      round % players.length
    }
  }

  def nextRound(): Int = {
    round += 1
    round
  }

  def updateTotalRound(): Int = {
    totalRound = (round / players.length).floor.toInt
    totalRound
  }

  def setMrXVisibility(): Boolean = {
    println(totalRound)
    val playerMrX = players(0)
    playerMrX.asInstanceOf[MrX].hidden = checkMrXVisibility()
    if(!playerMrX.asInstanceOf[MrX].hidden) {
      playerMrX.asInstanceOf[MrX].lastSeen = getCurrentPlayer().getPosition().number.toString
    }
    playerMrX.asInstanceOf[MrX].hidden
  }

  def checkMrXVisibility(): Boolean = {
    totalRound match {
      case 3 => return true
      case 8 => return true
      case 13 => return true
      case 18 => return true
      case 24 => return true
    }
    return false
  }

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
  //TODO: check if another player stands on the station
    if(ticketType.equals(TicketType.Taxi)) {
      if(getCurrentPlayer().taxiTickets <= 0) {
        return false
      }
      if(!getCurrentPlayer().getPosition().neighbourTaxis.contains(stations(newPosition))){
        return false
      }
    } else if(ticketType.equals(TicketType.Bus)) {
      if(getCurrentPlayer().busTickets <= 0) {
        return false
      }
      if(!getCurrentPlayer().getPosition().neighbourBuses.contains(stations(newPosition))){
        return false
      }
    } else {
      if(getCurrentPlayer().undergroundTickets <= 0) {
        return false
      }
      if(!getCurrentPlayer().getPosition().neighbourUndergrounds.contains(stations(newPosition))){
        return false
      }
    }
    true
  }

  def updatePlayerPosition(newPosition: Int, ticketType: TicketType): Station = {

    if(ticketType.equals(TicketType.Taxi)) {
      getCurrentPlayer().taxiTickets -= 1
    } else if(ticketType.equals(TicketType.Bus)){
      getCurrentPlayer().busTickets -= 1
    } else {
      getCurrentPlayer().undergroundTickets -= 1
    }
    getCurrentPlayer().station = stations(newPosition)
    getCurrentPlayer().station
  }

}
