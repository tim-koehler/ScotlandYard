package de.htwg.se.scotlandyard.controller.controllerBaseImpl

import de.htwg.se.scotlandyard.model.{GameModel, Station}

class UndoManager(gameModel: GameModel) {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil

  def doStep(command: Command): Station = {
    undoStack = command::undoStack
    command.doStep(gameModel)
  }

  def undoStep(): Station  = {
    undoStack match {
      case  Nil => gameModel.stations.head
      case head::stack =>
        val station = head.undoStep(gameModel)
        undoStack=stack
        redoStack= head::redoStack
        station
    }
  }

  def redoStep(): Station = {
    redoStack match {
      case Nil => gameModel.stations.head
      case head::stack =>
        val station = head.redoStep(gameModel)
        redoStack=stack
        undoStack=head::undoStack
        station
    }
  }
}