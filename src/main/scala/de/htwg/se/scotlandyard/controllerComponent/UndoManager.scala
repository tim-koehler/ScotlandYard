package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.Station
import de.htwg.se.scotlandyard.model.coreComponent.GameMaster

class UndoManager {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil

  def doStep(command: Command): Station = {
    undoStack = command::undoStack
    command.doStep
  }

  def undoStep(): Station  = {
    undoStack match {
      case  Nil => GameMaster.stations.head
      case head::stack => {
        val station = head.undoStep
        undoStack=stack
        redoStack= head::redoStack
        station
      }
    }
  }

  def redoStep(): Station = {
    redoStack match {
      case Nil => GameMaster.stations.head
      case head::stack => {
        val station = head.redoStep
        redoStack=stack
        undoStack=head::undoStack
        station
      }
    }
  }
}