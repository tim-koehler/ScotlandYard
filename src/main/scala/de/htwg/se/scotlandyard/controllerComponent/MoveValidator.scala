package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.GameModel.stations
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType, TicketType}
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.MrXInterface

import scala.util.control.Breaks.{break, breakable}

object MoveValidator {

  def validateMove(newPosition: Int, ticketType: TicketType): Boolean = {
    if (!isTargetStationInBounds(newPosition)) return false
    if (GameModel.getCurrentPlayer().station.number == newPosition) return false
    if (!isMeanOfTransportValid(newPosition, ticketType)) return false
    if (!isTargetStationEmpty(newPosition)) return false
    true
  }

  private def isTargetStationInBounds(newPosition: Int): Boolean = {
    newPosition < GameModel.stations.size && newPosition > 0
  }

  private def isMeanOfTransportValid(newPosition: Integer, ticketType: TicketType): Boolean = {
    val player = GameModel.getCurrentPlayer()
    ticketType match {
      case TicketType.Taxi =>
        isTransportMoveValid(newPosition)(player.tickets.taxiTickets, player.station.getNeighbourTaxis)
      case TicketType.Bus =>
        if (player.station.stationType == StationType.Taxi) return false
        isTransportMoveValid(newPosition)(player.tickets.busTickets, player.station.getNeighbourBuses)
      case TicketType.Underground =>
        if (player.station.stationType != StationType.Underground) return false
        isTransportMoveValid(newPosition)(player.tickets.undergroundTickets, player.station.getNeighbourUndergrounds)
      case _ =>
        if (!player.equals(GameModel.players.head)) return false
        isBlackMoveValid(newPosition)
    }
  }

  private def isTargetStationEmpty(newPosition: Integer): Boolean = {
    for ((p, index) <- GameModel.players.zipWithIndex) {
      breakable {
        if (index == 0 && !GameModel.getCurrentPlayer().equals(GameModel.getMrX())) break
        if (p.station.number == newPosition) return false
      }
    }
    true
  }

  private def isTransportMoveValid(newPosition: Int)(tickets: Int, neighbours: Set[Station]): Boolean = {
    if (tickets <= 0) return false
    neighbours.contains(stations(newPosition))
  }

  private def isBlackMoveValid(newPosition: Int): Boolean = {
    if (GameModel.getCurrentPlayer().asInstanceOf[MrXInterface].tickets.blackTickets <= 0) return false
    GameModel.getCurrentPlayer().station.getNeighbourTaxis.contains(stations(newPosition)) ||
      GameModel.getCurrentPlayer().station.getNeighbourBuses.contains(stations(newPosition)) ||
      GameModel.getCurrentPlayer().station.getNeighbourUndergrounds.contains(stations(newPosition))
  }

}
