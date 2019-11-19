package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType

object GameMaster {
  val defaultStation = new Station(0, StationType.Taxi, null, null, null)
  var stations: List[Station] = List()
  var players: List[Player] = List()
  var round = 1

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

  def nextRound(): Unit = {
    round += 1
  }

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {

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
