package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.model.{GameModel, Station}

class UndoManager() {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil

  def doStep(command: Command, gameModel: GameModel): GameModel = {
    undoStack = command::undoStack
    command.doStep(gameModel)
  }

  def undoStep(gameModel: GameModel): GameModel  = {
    undoStack match {
      case  Nil => gameModel
      case head::stack =>
        val gameModelTmp = head.undoStep(gameModel)
        undoStack=stack
        redoStack= head::redoStack
        gameModelTmp
    }
  }

  def redoStep(gameModel: GameModel): GameModel = {
    redoStack match {
      case Nil => gameModel
      case head::stack =>
        val gameModelTmp = head.redoStep(gameModel)
        redoStack=stack
        undoStack=head::undoStack
        gameModelTmp
    }
  }
}