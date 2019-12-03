package de.htwg.se.scotlandyard.util

trait Command {
  def doStep(): Boolean
  def undoStep(): Boolean
  def redoStep(): Boolean
}

