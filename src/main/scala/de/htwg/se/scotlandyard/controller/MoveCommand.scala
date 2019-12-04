package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.map.station.Station
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Command

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Station = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }

  override def undoStep(): Station = {
    GameMaster.previousRound()
    GameMaster.updatePlayerPosition(currentPosition)
    GameMaster.increaseTickets(ticketType)
    GameMaster.getCurrentPlayer().getPosition()
    }

  override def redoStep(): Station = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }
}
