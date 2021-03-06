package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.players.Detective
import de.htwg.se.scotlandyard.model.{GameModel, StationType}

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command {

  def incrementValue(x: Int): Int = {x + 1}
  def decrementValue(x: Int): Int = {x - 1}

  private def defaultDo(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    if (gameModelTmp.getCurrentPlayerIndex(gameModelTmp.players, gameModelTmp.round) == 0) {
      val newMrX = gameModelTmp.getMrX(gameModelTmp.players).addToHistory(gameModelTmp.getMrX(gameModelTmp.players), ticketType)
      gameModelTmp = gameModelTmp.copy(players = gameModelTmp.players.updated(0, newMrX))
    }
    gameModelTmp = gameModelTmp.updatePlayerPosition(gameModelTmp, newPosition)
    gameModelTmp = gameModelTmp.updateTickets(gameModelTmp, ticketType)(decrementValue)
    nextRound(gameModelTmp)
  }

  private def nextRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    gameModelTmp = updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp, incrementValue)
    if (!checkIfPlayerIsAbleToMove(gameModelTmp)) {
      val newPlayer = gameModelTmp.getCurrentPlayer(gameModelTmp.players, gameModelTmp.round).asInstanceOf[Detective].copy(isStuck = true)
      gameModelTmp = gameModelTmp.copy(
        players = gameModelTmp.players.updated(gameModelTmp.getCurrentPlayerIndex(gameModelTmp.players, gameModelTmp.round), newPlayer)
      )
      if (gameModelTmp.getDetectives(gameModelTmp.players).count(p => p.isStuck) == gameModelTmp.getDetectives(gameModelTmp.players).size) {
        gameModelTmp = gameModelTmp.setAllPlayersStuck(gameModelTmp)
      } else {
        gameModelTmp = nextRound(gameModelTmp)
      }
    }
    gameModelTmp
  }


  private def updateMrXVisibility(gameModel: GameModel): GameModel = {
    var mrX = gameModel.getMrX(gameModel.players).updateVisibility(gameModel.getMrX(gameModel.players), gameModel.MRX_VISIBLE_ROUNDS.contains(gameModel.totalRound))
    if (mrX.isVisible) {
      mrX = mrX.updateLastSeen(mrX, gameModel.players.head.station.toString)
    }
    gameModel.copy(players = gameModel.players.updated(0, mrX))
  }

  private def checkIfPlayerIsAbleToMove(gameModel: GameModel): Boolean = {
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    gameModel.stations(currentPlayer.station).stationType match {
      case StationType.Taxi =>
        currentPlayer.tickets.taxiTickets > 0
      case model.StationType.Bus =>
        currentPlayer.tickets.taxiTickets > 0 || currentPlayer.tickets.busTickets > 0
      case model.StationType.Underground =>
        currentPlayer.tickets.taxiTickets > 0 || currentPlayer.tickets.busTickets > 0 || currentPlayer.tickets.undergroundTickets > 0
    }
  }

  override def doStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }

  override def undoStep(gameModel: GameModel): GameModel = {
    if (gameModel.totalRound == 1 && gameModel.round == 1) {
      return gameModel
    }
    var gameModelTmp = previousRound(gameModel)
    if (gameModelTmp.getCurrentPlayerIndex(gameModelTmp.players, gameModelTmp.round) == 0) {
      val newMrX = gameModelTmp.getMrX(gameModelTmp.players).removeFromHistory(gameModelTmp.getMrX(gameModelTmp.players))
      gameModelTmp = gameModelTmp.copy(players = gameModelTmp.players.updated(0, newMrX))
    }
    gameModelTmp = gameModelTmp.updatePlayerPosition(gameModelTmp, currentPosition)
    gameModelTmp = gameModelTmp.updateTickets(gameModelTmp, ticketType)(incrementValue)
    gameModelTmp
  }

  private def previousRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    gameModelTmp = updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp, decrementValue)
    gameModelTmp
  }

  override def redoStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }
}
