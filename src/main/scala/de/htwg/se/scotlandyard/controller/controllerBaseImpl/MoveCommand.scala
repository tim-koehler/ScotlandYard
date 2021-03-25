package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.ScotlandYard.injector
import de.htwg.se.scotlandyard.controller.ControllerInterface
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.{GameModel, Station}

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  private def defaultDo(gameModel: GameModel): Station = {
    if(gameModel.getCurrentPlayerIndex == 0) {
      gameModel.getMrX.addToHistory(ticketType)
    }
    gameModel.updatePlayerPosition(newPosition)
    gameModel.updateTickets(ticketType)(gameModel.incrementTickets)

    val newStation = gameModel.getCurrentPlayer.station
    controller.nextRound()
    newStation
  }

  override def doStep(gameModel: GameModel): Station = {
    defaultDo(gameModel)
  }

  override def undoStep(gameModel: GameModel): Station = {
    if(gameModel.round == 0) {
      return gameModel.getCurrentPlayer.station
    }
    if(gameModel.getCurrentPlayerIndex == 1) {
      gameModel.getMrX.removeFromHistory()
    }
    controller.previousRound()
    gameModel.updatePlayerPosition(currentPosition)
    gameModel.updateTickets(ticketType)(gameModel.incrementTickets)
    gameModel.getCurrentPlayer.station
    }

  override def redoStep(gameModel: GameModel): Station = {
    defaultDo(gameModel)
  }
}
