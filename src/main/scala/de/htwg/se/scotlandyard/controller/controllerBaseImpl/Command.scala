package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.model.{GameModel, Station}

trait Command {
  def doStep(gameModel: GameModel): Station

  def undoStep(gameModel: GameModel): Station

  def redoStep(gameModel: GameModel): Station
}
