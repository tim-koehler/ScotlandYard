package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.ScotlandYard.injector
import de.htwg.se.scotlandyard.controller.{ControllerInterface, PlayerWin}
import de.htwg.se.scotlandyard.model
import de.htwg.se.scotlandyard.model.TicketType.TicketType
import de.htwg.se.scotlandyard.model.playersComponent.DetectiveInterface
import de.htwg.se.scotlandyard.model.{GameModel, Station, StationType}

class MoveCommand(currentPosition: Int, newPosition: Int, ticketType: TicketType) extends Command{

  val controller: ControllerInterface = injector.getInstance(classOf[ControllerInterface])

  private def defaultDo(gameModel: GameModel): GameModel = {
    if(gameModel.getCurrentPlayerIndex == 0) {
      gameModel.getMrX.addToHistory(ticketType)
    }
    gameModel.updatePlayerPosition(newPosition)
    gameModel.updateTickets(ticketType)(gameModel.decrementValue)
    nextRound(gameModel)
  }

  private def nextRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp.incrementValue)
    if (!checkIfPlayerIsAbleToMove(gameModelTmp)) {
      gameModelTmp = gameModelTmp.addStuckPlayer()
      if (gameModelTmp.stuckPlayers.size == gameModelTmp.players.size - 1) {
        gameModelTmp = gameModelTmp.setAllPlayerStuck()
      } else {
        nextRound(gameModelTmp)
      }
    }
    gameModelTmp
  }

  def previousRound(gameModel: GameModel): GameModel = {
    var gameModelTmp = gameModel
    updateMrXVisibility(gameModelTmp)
    gameModelTmp = gameModelTmp.updateRound(gameModelTmp.decrementValue)
    gameModelTmp
  }

  private def updateMrXVisibility(gameModel: GameModel): Boolean = {
    val mrX = gameModel.getMrX
    mrX.isVisible = gameModel.MRX_VISIBLE_ROUNDS.contains(gameModel.totalRound)
    if (mrX.isVisible) {
      mrX.lastSeen = gameModel.players.head.station.number.toString
    }
    mrX.isVisible
  }

  private def checkIfPlayerIsAbleToMove(gameModel: GameModel): Boolean = {
    gameModel.getCurrentPlayer.station.stationType match {
      case StationType.Taxi =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0
      case model.StationType.Bus =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0 || gameModel.getCurrentPlayer.tickets.busTickets > 0
      case model.StationType.Underground =>
        gameModel.getCurrentPlayer.tickets.taxiTickets > 0 || gameModel.getCurrentPlayer.tickets.busTickets > 0 || gameModel.getCurrentPlayer.tickets.undergroundTickets > 0
    }
  }

  override def doStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }

  override def undoStep(gameModel: GameModel): GameModel = {
    if(gameModel.round == 0) {
      return gameModel
    }
    if(gameModel.getCurrentPlayerIndex == 1) {
      gameModel.getMrX.removeFromHistory()
    }
    previousRound(gameModel)
    gameModel.updatePlayerPosition(currentPosition)
    gameModel.updateTickets(ticketType)(gameModel.incrementValue)
    gameModel
    }

  override def redoStep(gameModel: GameModel): GameModel = {
    defaultDo(gameModel)
  }
}
