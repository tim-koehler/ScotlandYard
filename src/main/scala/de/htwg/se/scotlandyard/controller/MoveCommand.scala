package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.player.MrX
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Command

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getCurrentPlayer().asInstanceOf[MrX].addToHistory(ticketType)
    }
    println(GameMaster.getCurrentPlayerIndex())
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }

  override def undoStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 1) {
      GameMaster.players(0).asInstanceOf[MrX].removeFromHistory()
    }
    GameMaster.previousRound()
    GameMaster.updatePlayerPosition(currentPosition)
    GameMaster.increaseTickets(ticketType)
    GameMaster.getCurrentPlayer().getPosition()
    }

  override def redoStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getCurrentPlayer().asInstanceOf[MrX].addToHistory(ticketType)
    }
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }
}
