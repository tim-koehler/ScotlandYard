package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.model.{GameModel, Station}

trait Command {
  def doStep(gameModel: GameModel): GameModel

  def undoStep(gameModel: GameModel): GameModel

  def redoStep(gameModel: GameModel): GameModel
}
