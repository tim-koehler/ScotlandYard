package de.htwg.se.scotlandyard.model.core

import de.htwg.se.scotlandyard.model.map._
import de.htwg.se.scotlandyard.model.player._
import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.model.player.TicketType
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType

object GameMaster {
  var stations: List[Station] = List()
  var players: List[Player] = List()
  var round = 1 // insgesamte Anzahl an Zuegen (wird um 1 erhoeht, wenn ein Spieler gezogen hat)
  var totalRound = 1 // Anzahl an Runden (wird um 1 erhoeht, wenn alle Spieler einmal dran waren)

  def startGame(): Boolean = {
    if(!GameInitializer.initialize()) {
      return false
    }
    true
  }

  def getCurrentPlayer(): Player = {
    players(getCurrentPlayerIndex())
  }

  def getCurrentPlayerIndex(): Int = {
    if(round % players.length == 0) {
      (players.length - 1)
    } else {
      ((round % players.length) - 1)
    }
  }

  def nextRound(): Int = {
    round += 1
    updateTotalRound()
    updateMrXVisibility()
    round
  }

  def updateTotalRound(): Int = {
    totalRound = (round.toDouble / players.length.toDouble).ceil.toInt
    totalRound
  }

  def updateMrXVisibility(): Boolean = {
    val playerMrX = players(0)
    playerMrX.asInstanceOf[MrX].isVisible = checkMrXVisibility()
    if(playerMrX.asInstanceOf[MrX].isVisible) {
      playerMrX.asInstanceOf[MrX].lastSeen = players(0).getPosition().number.toString
    }
    playerMrX.asInstanceOf[MrX].isVisible
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

  // TODO: Method needs a lof of refactoring!
  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    if(newPosition >= GameMaster.stations.size) {
      return false
    }

    // TODO: Refactoring with numbers behind enums
    if(GameMaster.getCurrentPlayer().getPosition().sType.equals(StationType.Taxi)) {
      if(ticketType != TicketType.Taxi){
        return false
      }
    } else if(GameMaster.getCurrentPlayer().getPosition().sType.equals(StationType.Bus)) {
      if(ticketType == TicketType.Underground){
        return false
      }
    }

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
    // TODO: Does index 0 mean MrX ?
    if(GameMaster.getCurrentPlayerIndex() != 0) {
      for(p <- GameMaster.players){
        if(!p.name.equals("MrX")) {
          if(p.getPosition().number == newPosition){
            return false
          }
        }
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
