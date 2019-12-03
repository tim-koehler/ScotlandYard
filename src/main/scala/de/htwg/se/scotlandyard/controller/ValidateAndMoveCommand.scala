package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.player.TicketType.TicketType
import de.htwg.se.scotlandyard.util.Command

class ValidateAndMoveCommand(controller: Controller, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Boolean = {
    if (GameMaster.validateMove(newPosition, ticketType)) {
      GameMaster.updatePlayerPosition(newPosition)
      GameMaster.decreaseTickets(ticketType)
      controller.nextRound()
      controller.notifyObservers
      return true
    }
    controller.notifyObservers
    false
  }

  override def undoStep(): Boolean = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.increaseTickets(ticketType)
    controller.previousRound()
    controller.notifyObservers
    true
    }

  override def redoStep(): Boolean = {
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    controller.nextRound()
    controller.notifyObservers
    true
  }
}
