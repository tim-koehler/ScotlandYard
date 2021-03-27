package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.ScotlandYard.injector
import de.htwg.se.scotlandyard.controller.{ControllerInterface, PlayerWin}
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType}

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command {

  private def defaultDo(gameModel: GameModel): GameModel = {
    if (gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round) == 0) {
      gameModel.getMrX(gameModel.players).addToHistory(ticketType)
    }
    val currentPlayer = gameModel.getCurrentPlayer(gameModel.players, gameModel.round)
    gameModel.updatePlayerPosition(currentPlayer, newPosition)
    gameModel.updateTickets(currentPlayer, ticketType)(gameModel.decrementValue)
    nextRound(gameModel)
  }

  private def nextRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    updateMrXVisibility(gameModelTmp)
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

  def previousRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp, gameModelTmp.decrementValue)
    gameModelTmp
  }

  private def updateMrXVisibility(gameModel: GameModel): Boolean = {
    val mrX = gameModel.getMrX(gameModel.players)
    mrX.isVisible = gameModel.MRX_VISIBLE_ROUNDS.contains(gameModel.totalRound)
    if (mrX.isVisible) {
      mrX.lastSeen = gameModel.players.head.station.number.toString
    }
    mrX.isVisible
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
    if (gameModel.getCurrentPlayerIndex(gameModel.players, gameModel.round) == 1) {
      gameModel.getMrX(gameModel.players).removeFromHistory()
    }
    val gameModelTmp = previousRound(gameModel)

    val currentPlayer = gameModelTmp.getCurrentPlayer(gameModelTmp.players, gameModelTmp.round)
    gameModelTmp.updatePlayerPosition(currentPlayer, currentPosition)
    gameModelTmp.updateTickets(currentPlayer, ticketType)(gameModelTmp.incrementValue)
    gameModelTmp
  }

  override def redoStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }
}
