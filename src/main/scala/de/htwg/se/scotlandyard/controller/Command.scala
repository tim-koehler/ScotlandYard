package de.htwg.se.scotlandyard.controller

import de.htwg.se.scotlandyard.model.Station

trait Command {
  def doStep(): Station

  def undoStep(): Station

  def redoStep(): Station
}
