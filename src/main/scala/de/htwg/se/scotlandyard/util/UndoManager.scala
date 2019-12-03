package de.htwg.se.scotlandyard.util

import de.htwg.se.scotlandyard.model.core.GameMaster
import de.htwg.se.scotlandyard.model.map.station.Station

class UndoManager {
  private var undoStack: List[Command]= Nil
  private var redoStack: List[Command]= Nil

  def doStep(command: Command): Station = {
    undoStack = command::undoStack
    command.doStep
  }

  def undoStep(): Station  = {
    undoStack match {
      case  Nil => GameMaster.stations(0)
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
      case Nil => GameMaster.stations(0)
      case head::stack => {
        val station = head.redoStep
        redoStack=stack
        undoStack=head::undoStack
        station
      }
    }
  }
}