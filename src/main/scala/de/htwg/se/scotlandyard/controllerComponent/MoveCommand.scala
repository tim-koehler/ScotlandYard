package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.ScotlandYard.injector
import de.htwg.se.scotlandyard.model.{GameModel, Station}
import de.htwg.se.scotlandyard.model.TicketType.TicketType

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  private def defaultDo(): Station = {
    if(GameModel.getCurrentPlayerIndex() == 0) {
      GameModel.getMrX().addToHistory(ticketType)
    }
    GameModel.updatePlayerPosition(newPosition)
    GameModel.decreaseTickets(ticketType)

    controller.nextRound()
    GameModel.getCurrentPlayer().station
  }

  override def doStep(): Station = {
    defaultDo()
  }

  override def undoStep(): Station = {
    if(GameModel.round == 0) {
      return GameModel.getCurrentPlayer().station
    }
    if(GameModel.getCurrentPlayerIndex() == 1) {
      GameModel.getMrX().removeFromHistory()
    }
    controller.previousRound()
    GameModel.updatePlayerPosition(currentPosition)
    GameModel.increaseTickets(ticketType)
    GameModel.getCurrentPlayer().station
    }

  override def redoStep(): Station = {
    defaultDo()
  }
}
