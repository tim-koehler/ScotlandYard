package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.ScotlandYard.injector
import de.htwg.se.scotlandyard.controller.{ControllerInterface, PlayerWin}
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType}

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command {

  private def defaultDo(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    if (gameModelTmp.getCurrentPlayerIndex(gameModelTmp.players, gameModelTmp.round) == 0) {
      val newMrX = gameModelTmp.getMrX(gameModelTmp.players).addToHistory(gameModelTmp.getMrX(gameModelTmp.players), ticketType)
      gameModelTmp = gameModelTmp.copy(players = gameModelTmp.players.updated(0, newMrX))
    }
    gameModelTmp = gameModelTmp.updatePlayerPosition(gameModelTmp, newPosition)
    gameModelTmp = gameModelTmp.updateTickets(gameModelTmp, ticketType)(gameModelTmp.decrementValue)
    nextRound(gameModelTmp)
  }

  private def nextRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    gameModelTmp = updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp, gameModelTmp.incrementValue)
    if (!checkIfPlayerIsAbleToMove(gameModelTmp)) {
      gameModelTmp = gameModelTmp.addStuckPlayer(gameModelTmp, gameModelTmp.getCurrentPlayer(gameModelTmp.players, gameModelTmp.round))
      if (gameModelTmp.stuckPlayers.size == gameModelTmp.players.size - 1) {
        gameModelTmp = gameModelTmp.setAllPlayerStuck(gameModelTmp)
      } else {
        gameModelTmp = nextRound(gameModelTmp)
      }
    }
    gameModelTmp
  }


  private def updateMrXVisibility(gameModel: GameModel): GameModel = {
    var mrX = gameModel.getMrX(gameModel.players).updateVisibility(gameModel.getMrX(gameModel.players), gameModel.MRX_VISIBLE_ROUNDS.contains(gameModel.totalRound))
    if (mrX.isVisible) {
      mrX = mrX.updateLastSeen(mrX, gameModel.players.head.station.number.toString)
    }
    gameModel.copy(players = gameModel.players.updated(0, mrX))
  }

  private def checkIfPlayerIsAbleToMove(gameModel: GameModel): Boolean = {
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    currentPlayer.station.stationType match {
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
    gameModelTmp = gameModelTmp.updateTickets(gameModelTmp, ticketType)(gameModelTmp.incrementValue)
    gameModelTmp
  }

  def previousRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    gameModelTmp = updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp, gameModelTmp.decrementValue)
    gameModelTmp
  }

  override def redoStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }
}
