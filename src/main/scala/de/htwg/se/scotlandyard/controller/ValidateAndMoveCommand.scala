package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Command

class ValidateAndMoveCommand(controller: Controller, currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Boolean = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    controller.nextRound()
    true
  }

  override def undoStep(): Boolean = {
    controller.previousRound()
    GameMaster.updatePlayerPosition(currentPosition)
    GameMaster.increaseTickets(ticketType)
    true
    }

  override def redoStep(): Boolean = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    controller.nextRound()
    true
  }
}
