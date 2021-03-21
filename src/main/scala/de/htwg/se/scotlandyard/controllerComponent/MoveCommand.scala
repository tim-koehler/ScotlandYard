package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.Station
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.TicketType.TicketType

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getMrX().addToHistory(ticketType)
    }
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().station
    GameMaster.nextRound()
    station
  }

  override def undoStep(): Station = {
    if(GameMaster.round == 0) {
      return GameMaster.getCurrentPlayer().station
    }
    if(GameMaster.getCurrentPlayerIndex() == 1) {
      GameMaster.getMrX().removeFromHistory()
    }
    GameMaster.previousRound()
    GameMaster.updatePlayerPosition(currentPosition)
    GameMaster.increaseTickets(ticketType)
    GameMaster.getCurrentPlayer().station
    }

  override def redoStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getMrX().addToHistory(ticketType)
    }
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().station
    GameMaster.nextRound()
    station
  }
}
