package de.htwg.se.scotlandyard.controllerComponent.controllerBaseImpl

import de.htwg.se.scotlandyard.model.coreComponent.GameMaster
import de.htwg.se.scotlandyard.model.tuiMapComponent.station.Station
import de.htwg.se.scotlandyard.model.playersComponent.playersBaseImpl.MrX
import de.htwg.se.scotlandyard.util.Command
import de.htwg.se.scotlandyard.util.TicketType.TicketType

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  override def doStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getMrX().addToHistory(ticketType)
    }
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }

  override def undoStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 1) {
      GameMaster.getMrX().removeFromHistory()
    }
    GameMaster.previousRound()
    GameMaster.updatePlayerPosition(currentPosition)
    GameMaster.increaseTickets(ticketType)
    GameMaster.getCurrentPlayer().getPosition()
    }

  override def redoStep(): Station = {
    if(GameMaster.getCurrentPlayerIndex() == 0) {
      GameMaster.getMrX().addToHistory(ticketType)
    }
    GameMaster.updatePlayerPosition(newPosition)
    GameMaster.decreaseTickets(ticketType)
    val station = GameMaster.getCurrentPlayer().getPosition()
    GameMaster.nextRound()
    station
  }
}
