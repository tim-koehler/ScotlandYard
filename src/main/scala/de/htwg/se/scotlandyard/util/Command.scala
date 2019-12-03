package de.htwg.se.scotlandyard.util

import de.htwg.se.scotlandyard.model.map.station.Station

trait Command {
  def doStep(): Station
  def undoStep(): Station
  def redoStep(): Station
}

