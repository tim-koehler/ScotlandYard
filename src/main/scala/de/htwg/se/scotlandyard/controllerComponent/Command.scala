package de.htwg.se.scotlandyard.controllerComponent

import de.htwg.se.scotlandyard.model.Station

trait Command {
  def doStep(): Station

  def undoStep(): Station

  def redoStep(): Station
}
